package org.mib.db.mybatis.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mib.db.model.Tag;
import org.mib.db.mybatis.mapper.TagMapper;
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
public class TagDao extends Dao {

    public TagDao(final SqlSessionFactory ssf) {
        super(ssf);
    }

    public Tag createTag(Tag tag) {
        validateObjectNotNull(tag, "tag");
        try (SqlSession session = ssf.openSession()) {
            TagMapper mapper = session.getMapper(TagMapper.class);
            mapper.createTag(tag);
            return tag;
        }
    }

    public Tag getTag(int id) {
        validateIntPositive(id, "tag id");
        try (SqlSession session = ssf.openSession()) {
            TagMapper mapper = session.getMapper(TagMapper.class);
            Tag tag = mapper.getTag(id);
            if (tag == null) throw new ResourceNotFoundException("no tag found for " + id);
            return tag;
        }
    }

    public ListPayload<Tag> listTags(ListElementRequest ler) {
        validateObjectNotNull(ler, "list config");
        validateLongNotNegative(ler.getOffset(), "list offset");
        validateLongNotNegative(ler.getLimit(), "list limit");
        try (SqlSession session = ssf.openSession()) {
            TagMapper mapper = session.getMapper(TagMapper.class);
            long total = mapper.countOfTags(ler);
            List<Tag> tags = mapper.listTags(ler);
            return ListPayload.<Tag>builder().list(tags).total(total).offset(ler.getOffset()).build();
        }
    }

    public Tag updateTag(Tag tag) {
        validateObjectNotNull(tag, "tag");
        validateIntPositive(tag.getId(), "tag id");
        try (SqlSession session = ssf.openSession()) {
            TagMapper mapper = session.getMapper(TagMapper.class);
            mapper.updateTag(tag);
            return tag;
        }
    }

    public void deleteTags(Collection<Integer> ids) {
        validateCollectionNotEmptyContainsNoNull(ids, "tag ids");
        try (SqlSession session = ssf.openSession()) {
            TagMapper mapper = session.getMapper(TagMapper.class);
            mapper.deleteTags(ids);
        }
    }
}
