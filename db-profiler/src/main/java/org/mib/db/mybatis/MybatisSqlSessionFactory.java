package org.mib.db.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mib.common.config.ConfigProvider;

import java.io.FileInputStream;
import java.io.IOException;

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
                    try {
                        log.info("creating mybatis sql session factory from config {} with env {}...", configFile, configEnv);
                        factory = new SqlSessionFactoryBuilder().build(new FileInputStream(configFile), configEnv);
                    } catch (IOException e) {
                        log.error("failed to read mybatis config from {}", configFile, e);
                        throw new RuntimeException("can't create mybatis sql session factory", e);
                    }
                }
            }
        }
        return factory;
    }
}
