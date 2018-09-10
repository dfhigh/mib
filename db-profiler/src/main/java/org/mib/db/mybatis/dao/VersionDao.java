package org.mib.db.mybatis.dao;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mib.db.model.Version;
import org.mib.db.mybatis.mapper.VersionMapper;
import org.mib.rest.exception.ResourceNotFoundException;
import org.mib.rest.model.ListElementRequest;
import org.mib.rest.model.ListPayload;

import java.util.Collection;
import java.util.List;

import static org.mib.common.validator.Validator.validateCollectionNotEmptyContainsNoNull;
import static org.mib.common.validator.Validator.validateIntPositive;
import static org.mib.common.validator.Validator.validateLongNotNegative;
import static org.mib.common.validator.Validator.validateObjectNotNull;

@Slf4j
public class VersionDao extends Dao {

    public VersionDao(SqlSessionFactory ssf) {
        super(ssf);
    }

    public Version createVersion(Version version) {
        validateObjectNotNull(version, "version");
        try (SqlSession session = ssf.openSession()) {
            VersionMapper mapper = session.getMapper(VersionMapper.class);
            mapper.createVersion(version);
            return version;
        }
    }

    public Version getVersion(int id) {
        validateIntPositive(id, "version id");
        try (SqlSession session = ssf.openSession()) {
            VersionMapper mapper = session.getMapper(VersionMapper.class);
            Version version = mapper.getVersion(id);
            if (version == null) throw new ResourceNotFoundException("no version found for " + id);
            return version;
        }
    }

    public ListPayload<Version> listVersions(int documentId, ListElementRequest ler) {
        validateIntPositive(documentId, "document id");
        validateObjectNotNull(ler, "list config");
        validateLongNotNegative(ler.getOffset(), "list offset");
        validateLongNotNegative(ler.getLimit(), "list limit");
        if (ler.getParams() == null) ler.setParams(Maps.newHashMapWithExpectedSize(1));
        ler.getParams().put("document_id", documentId);
        try (SqlSession session = ssf.openSession()) {
            VersionMapper mapper = session.getMapper(VersionMapper.class);
            long total = mapper.countOfVersions(ler);
            List<Version> versions = mapper.listVersions(ler);
            return ListPayload.<Version>builder().list(versions).total(total).offset(ler.getOffset()).build();
        }
    }

    public Version updateVersion(Version version) {
        validateObjectNotNull(version, "version");
        validateIntPositive(version.getId(), "version id");
        try (SqlSession session = ssf.openSession()) {
            VersionMapper mapper = session.getMapper(VersionMapper.class);
            mapper.updateVersion(version);
            return version;
        }
    }

    public void deleteVersions(Collection<Integer> ids) {
        validateCollectionNotEmptyContainsNoNull(ids, "version ids");
        try (SqlSession session = ssf.openSession()) {
            VersionMapper mapper = session.getMapper(VersionMapper.class);
            mapper.deleteVersions(ids);
        }
    }
}
