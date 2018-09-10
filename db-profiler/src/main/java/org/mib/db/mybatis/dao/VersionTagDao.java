package org.mib.db.mybatis.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mib.db.model.Tag;
import org.mib.db.model.Version;
import org.mib.db.mybatis.mapper.VersionTagMapper;
import org.mib.rest.model.ListPayload;

import java.util.List;

import static org.mib.common.validator.Validator.validateIntPositive;
import static org.mib.common.validator.Validator.validateLongNotNegative;

@Slf4j
public class VersionTagDao extends Dao {

    public VersionTagDao(final SqlSessionFactory ssf) {
        super(ssf);
    }

    public void createVersionTag(int versionId, int tagId) {
        validateIntPositive(versionId, "version id");
        validateIntPositive(tagId, "tag id");
        try (SqlSession session = ssf.openSession()) {
            VersionTagMapper mapper = session.getMapper(VersionTagMapper.class);
            mapper.createVersionTag(versionId, tagId);
        }
    }

    public ListPayload<Tag> listTagsForVersion(int versionId, int limit, long offset) {
        validateIntPositive(versionId, "version id");
        validateLongNotNegative(limit, "list limit");
        validateLongNotNegative(offset, "list offset");
        try (SqlSession session = ssf.openSession()) {
            VersionTagMapper mapper = session.getMapper(VersionTagMapper.class);
            long total = mapper.countOfVersionTags(versionId);
            List<Tag> tags = mapper.listTagsByVersion(versionId, limit, offset);
            return ListPayload.<Tag>builder().list(tags).total(total).offset(offset).build();
        }
    }

    public ListPayload<Version> listTaggedVersions(int tagId, int limit, int offset) {
        validateIntPositive(tagId, "tag id");
        validateLongNotNegative(limit, "list limit");
        validateLongNotNegative(offset, "list offset");
        try (SqlSession session = ssf.openSession()) {
            VersionTagMapper mapper = session.getMapper(VersionTagMapper.class);
            long total = mapper.countOfTaggedVersions(tagId);
            List<Version> versions = mapper.listVersionsByTag(tagId, limit, offset);
            return ListPayload.<Version>builder().list(versions).total(total).offset(offset).build();
        }
    }

    public void deTag(int versionId, int tagId) {
        validateIntPositive(versionId, "version id");
        validateIntPositive(tagId, "tag id");
        try (SqlSession session = ssf.openSession()) {
            VersionTagMapper mapper = session.getMapper(VersionTagMapper.class);
            mapper.deleteVersionTag(versionId, tagId);
        }
    }

    public void deTag(int tagId) {
        validateIntPositive(tagId, "tag id");
        try (SqlSession session = ssf.openSession()) {
            VersionTagMapper mapper = session.getMapper(VersionTagMapper.class);
            mapper.deleteTag(tagId);
        }
    }
}
