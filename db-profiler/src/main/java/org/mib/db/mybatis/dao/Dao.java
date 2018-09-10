package org.mib.db.mybatis.dao;

import org.apache.ibatis.session.SqlSessionFactory;

import static org.mib.common.validator.Validator.validateObjectNotNull;

abstract class Dao {

    final SqlSessionFactory ssf;

    Dao(final SqlSessionFactory ssf) {
        validateObjectNotNull(ssf, "sql session factory");
        this.ssf = ssf;
    }
}
