package org.mib.db.entry;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.session.SqlSessionFactory;
import org.flywaydb.core.Flyway;
import org.mib.db.model.Document;
import org.mib.db.model.Folder;
import org.mib.db.model.Project;
import org.mib.db.model.Tag;
import org.mib.db.model.Version;
import org.mib.db.model.factory.ModelFactory;
import org.mib.db.mybatis.MybatisSqlSessionFactory;
import org.mib.db.mybatis.dao.DocumentDao;
import org.mib.db.mybatis.dao.FolderDao;
import org.mib.db.mybatis.dao.ProjectDao;
import org.mib.db.mybatis.dao.TagDao;
import org.mib.db.mybatis.dao.VersionDao;
import org.mib.db.mybatis.dao.VersionTagDao;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.mib.common.config.ConfigProvider.get;
import static org.mib.common.config.ConfigProvider.getDouble;
import static org.mib.common.config.ConfigProvider.getInt;

@Slf4j
public class DBInitializer {

    public static void main(String[] args) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(get("db.url"), get("db.username"), get("db.password"));
        flyway.migrate();

        SqlSessionFactory ssf = MybatisSqlSessionFactory.getFactory();
        ProjectDao projectDao = new ProjectDao(ssf);
        FolderDao folderDao = new FolderDao(ssf);
        DocumentDao docDao = new DocumentDao(ssf);
        VersionDao versionDao = new VersionDao(ssf);
        TagDao tagDao = new TagDao(ssf);
        VersionTagDao versionTagDao = new VersionTagDao(ssf);

        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        final int projectCount = getInt("project.count"), folderCountMax = getInt("folder.count.max"),
                docCountMax = getInt("doc.count.max"), versionCountMax = getInt("version.count.max"),
                tagCount = getInt("tag.count"), tagVersionCountMax = getInt("tag_version.count.max");
        final double tagRate = getDouble("tag_version.rate");
        AtomicInteger totalFolderCount = new AtomicInteger(0), totalDocCount = new AtomicInteger(0),
                totalVersionCount = new AtomicInteger(0);
        List<Pair<Integer, AtomicInteger>> tags = Lists.newArrayListWithCapacity(tagCount);
        IntStream.range(0, tagCount).parallel().forEach(i -> {
            Tag tag = ModelFactory.randomTag();
            long start = System.currentTimeMillis();
            tagDao.createTag(tag);
            log.info("created tag {} in {} ms", tag.getId(), System.currentTimeMillis()-start);
            tags.add(Pair.of(tag.getId(), new AtomicInteger(0)));
        });
        IntStream.range(0, projectCount).parallel().forEach(i -> {
            Project project = ModelFactory.randomProject();
            long start0 = System.currentTimeMillis();
            project = projectDao.createProject(project);
            int projectId = project.getId();
            log.info("created project {} in {} ms", projectId, System.currentTimeMillis()-start0);
            final int folderCount = tlr.nextInt(folderCountMax);
            totalFolderCount.addAndGet(folderCount);
            IntStream.range(0, folderCount).parallel().forEach(j -> {
                Folder folder = ModelFactory.randomFolder(projectId);
                long start1 = System.currentTimeMillis();
                folder = folderDao.createFolder(folder);
                int folderId = folder.getId();
                log.info("created folder {} in {} ms", folderId, System.currentTimeMillis()-start1);
                final int docCount = tlr.nextInt(docCountMax);
                totalDocCount.addAndGet(docCount);
                IntStream.range(0, docCount).parallel().forEach(k -> {
                    Document document = ModelFactory.randomDocument(projectId, folderId);
                    long start2 = System.currentTimeMillis();
                    document = docDao.createDocument(document);
                    int docId = document.getId();
                    log.info("created document {} in {} ms", docId, System.currentTimeMillis()-start2);
                    final int versionCount = tlr.nextInt(versionCountMax);
                    totalVersionCount.addAndGet(versionCount);
                    IntStream.range(0, versionCount).parallel().forEach(l -> {
                        Version version = ModelFactory.randomVersion(projectId, folderId, docId);
                        long start3 = System.currentTimeMillis();
                        version = versionDao.createVersion(version);
                        int versionId = version.getId();
                        log.info("created version {} in {} ms", versionId, System.currentTimeMillis()-start3);
                        if (tlr.nextDouble() < tagRate) {
                            int attempts = 0;
                            while (attempts++ < 3) {
                                Pair<Integer, AtomicInteger> pair = tags.get(tlr.nextInt(tagCount));
                                if (pair.getRight().get() >= tagVersionCountMax) continue;
                                long start4 = System.currentTimeMillis();
                                versionTagDao.createVersionTag(versionId, pair.getLeft());
                                log.info("tag version {} with tag {} in {} ms", versionId, pair.getLeft(), System.currentTimeMillis() - start4);
                                pair.getRight().incrementAndGet();
                                break;
                            }
                        }
                    });
                });
            });
        });
        tags.forEach(pair -> log.info("tag {} has {} versions attached", pair.getLeft(), pair.getRight().get()));
        log.info("finished initializing db with {} projects, {} folders, {} docs, {} versions, {} tags", projectCount,
                totalFolderCount, totalDocCount, totalVersionCount, tagCount);
    }
}
