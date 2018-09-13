package org.mib.db.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mib.common.config.ConfigProvider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import static org.mib.common.validator.Validator.validateStringNotBlank;

@Slf4j
public class MybatisSqlSessionFactory {

    private static volatile SqlSessionFactory factory;

    public static SqlSessionFactory getFactory() {
        if (factory == null) {
            synchronized (MybatisSqlSessionFactory.class) {
                if (factory == null) {
                    String configFile = ConfigProvider.get("mybatis.config"), configEnv = ConfigProvider.get("mybatis.env");
                    validateStringNotBlank(configFile, "mybatis config file");
                    validateStringNotBlank(configEnv, "mybatis config env");
                    Properties properties = ConfigProvider.getByPrefix("db");
                    try {
                        log.info("creating mybatis sql session factory from config {} with env {} and properties {}...", configFile, configEnv, properties);
                        factory = new SqlSessionFactoryBuilder().build(new FileInputStream(configFile), configEnv, properties);
                    } catch (FileNotFoundException e) {
                        log.warn("{} does not exist as a filesystem file, try to load as classpath resource", configFile);
                        factory = new SqlSessionFactoryBuilder().build(MybatisSqlSessionFactory.class.getClassLoader().getResourceAsStream(configFile), configEnv, properties);
                    }
                }
            }
        }
        return factory;
    }
}
