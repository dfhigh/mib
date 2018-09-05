package org.mib.db.entry;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;

@Slf4j
public class DBProfiler {

    public static void main(String[] args) throws Exception {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:mysql://172.27.129.13:3306/mib?charset=utf-8&useSSL=false&createDatabaseIfNotExist=true", "root", "Ai4_every_1");
        flyway.migrate();
    }
}
