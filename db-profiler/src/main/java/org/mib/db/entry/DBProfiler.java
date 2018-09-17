package org.mib.db.entry;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mib.common.config.ConfigProvider;
import org.mib.db.model.Version;
import org.mib.db.mybatis.MybatisSqlSessionFactory;
import org.mib.db.mybatis.dao.VersionDao;
import org.mib.db.mybatis.dao.VersionTagDao;
import org.mib.rest.model.ListPayload;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Slf4j
public class DBProfiler {

    public static void main(String[] args) {
        SqlSessionFactory ssf = MybatisSqlSessionFactory.getFactory();
        VersionDao versionDao = new VersionDao(ssf);
        VersionTagDao versionTagDao = new VersionTagDao(ssf);

        final long profileDurationSeconds = ConfigProvider.getLong("profile.duration.seconds");
        final double updateRate = ConfigProvider.getDouble("profile.update.rate");

        long end = System.currentTimeMillis() + profileDurationSeconds * 1000;
        AtomicInteger reads = new AtomicInteger(0), updates = new AtomicInteger(0);
        while (System.currentTimeMillis() < end) {
            IntStream.range(0, 16).parallel().forEach(i -> {
                ThreadLocalRandom tlr = ThreadLocalRandom.current();
                int tagId = tlr.nextInt(1, 2001);
                int limit = tlr.nextInt(50, 101);
                int offset = tlr.nextInt(0, 980);
                long start0 = System.nanoTime();
                ListPayload<Version> lp = versionTagDao.listTaggedVersions(tagId, limit, offset);
                log.info("list:{}", System.nanoTime()-start0);
                reads.incrementAndGet();
                lp.getList().parallelStream().forEach(v -> {
                    ThreadLocalRandom local = ThreadLocalRandom.current();
                    if (local.nextDouble() > updateRate) return;
                    int batch = local.nextInt(15);
                    for (int j = 0; j < batch; j++) {
                        switch (local.nextInt(1, 15)) {
                            case 1:
                                v.setStrProp1(randomAlphanumeric(1, 256));
                                break;
                            case 2:
                                v.setStrProp2(randomAlphanumeric(1, 256));
                                break;
                            case 3:
                                v.setStrProp3(randomAlphanumeric(1, 256));
                                break;
                            case 4:
                                v.setStrProp4(randomAlphanumeric(1, 256));
                                break;
                            case 5:
                                v.setStrProp5(randomAlphanumeric(1, 256));
                                break;
                            case 6:
                                v.setStrProp6(randomAlphanumeric(1, 256));
                                break;
                            case 7:
                                v.setIntProp7(local.nextInt());
                                break;
                            case 8:
                                v.setIntProp8(local.nextInt());
                                break;
                            case 9:
                                v.setIntProp9(local.nextInt());
                                break;
                            case 10:
                                v.setIntProp10(local.nextInt());
                                break;
                            case 11:
                                v.setIntProp11(local.nextInt());
                                break;
                            case 12:
                                v.setIntProp12(local.nextInt());
                                break;
                            case 13:
                                v.setIntProp13(local.nextInt());
                                break;
                            case 14:
                                v.setIntProp14(local.nextInt());
                                break;
                            default:
                                break;
                        }
                    }
                    if (batch > 0) {
                        long start1 = System.nanoTime();
                        versionDao.updateVersion(v);
                        log.info("update:{}", System.nanoTime()-start1);
                        updates.incrementAndGet();
                    }
                });
            });
        }
        log.info("finished profiling with {} reads and {} updates in {} seconds", reads.get(), updates.get(), profileDurationSeconds);
    }
}
