package org.mib.db.entry;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.flywaydb.core.Flyway;
import org.mib.db.model.Document;
import org.mib.db.model.Folder;
import org.mib.db.model.Project;
import org.mib.db.model.Version;
import org.mib.db.model.factory.ModelFactory;
import org.mib.db.mybatis.MybatisSqlSessionFactory;
import org.mib.db.mybatis.dao.DocumentDao;
import org.mib.db.mybatis.dao.FolderDao;
import org.mib.db.mybatis.dao.ProjectDao;
import org.mib.db.mybatis.dao.TagDao;
import org.mib.db.mybatis.dao.VersionDao;
import org.mib.db.mybatis.dao.VersionTagDao;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class DBInitializer {

    public static void main(String[] args) throws Exception {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:mysql://172.27.129.13:3306/mib?charset=utf-8&useSSL=false&createDatabaseIfNotExist=true", "root", "Ai4_every_1");
        flyway.migrate();

        SqlSessionFactory ssf = MybatisSqlSessionFactory.getFactory();
        ProjectDao projectDao = new ProjectDao(ssf);
        FolderDao folderDao = new FolderDao(ssf);
        DocumentDao docDao = new DocumentDao(ssf);
        VersionDao versionDao = new VersionDao(ssf);
        TagDao tagDao = new TagDao(ssf);
        VersionTagDao versionTagDao = new VersionTagDao(ssf);

        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        final int projectCount = 100, folderCountMax = 100, docCountMax = 1000, versionCountMax = 100;
        for (int i = 0; i < projectCount; i++) {
            Project project = ModelFactory.randomProject();
            project = projectDao.createProject(project);
            log.info("created project {}", project.getId());
            final int folderCount = tlr.nextInt(folderCountMax);
            for (int j = 0; j < folderCount; j++) {
                Folder folder = ModelFactory.randomFolder(project.getId());
                folder = folderDao.createFolder(folder);
                log.info("created folder {}", folder.getId());
                final int docCount = tlr.nextInt(docCountMax);
                for (int k = 0; k < docCount; k++) {
                    Document document = ModelFactory.randomDocument(project.getId(), folder.getId());
                    document = docDao.createDocument(document);
                    log.info("created document {}", document.getId());
                    final int versionCount = tlr.nextInt(versionCountMax);
                    for (int l = 0; l < versionCount; l++) {
                        Version version = ModelFactory.randomVersion(project.getId(), folder.getId(), document.getId());
                        version = versionDao.createVersion(version);
                        log.info("created version {}", version.getId());
                    }
                }
            }
        }
    }
}
