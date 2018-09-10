package org.mib.db.entry;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.flywaydb.core.Flyway;
import org.mib.db.model.Project;
import org.mib.db.model.factory.ModelFactory;
import org.mib.db.mybatis.MybatisSqlSessionFactory;
import org.mib.db.mybatis.dao.ProjectDao;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class DBInitializer {

    public static void main(String[] args) throws Exception {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:mysql://172.27.129.13:3306/mib?charset=utf-8&useSSL=false&createDatabaseIfNotExist=true", "root", "Ai4_every_1");
        flyway.migrate();

        SqlSessionFactory ssf = MybatisSqlSessionFactory.getFactory();
        ProjectDao projectDao = new ProjectDao(ssf);

        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        final int projectCount = 100, folderCountMax = 100, docCountMax = 1000, versionCountMax = 100;
        for (int i = 0; i < projectCount; i++) {
            Project project = ModelFactory.randomProject();
            project = projectDao.createProject(project);
            log.info("created project {}", project.getId());
        }
    }
}
