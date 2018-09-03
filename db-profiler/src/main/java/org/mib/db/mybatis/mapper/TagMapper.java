package org.mib.db.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.mib.db.model.Tag;
import org.mib.rest.model.ListElementRequest;

import java.util.Collection;
import java.util.List;

@Mapper
public interface TagMapper {

    @Insert("insert into tag (name, description, created_at) values (#{tag.name}, #{tag.description}, #{tag.createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "tag.id")
    void createTag(@Param("tag") Tag tag);

    Tag getTag(@Param("id") int id);

    long countOfTags(@Param("params") ListElementRequest ler);

    List<Tag> listTags(@Param("params") ListElementRequest ler);

    void updateTag(@Param("tag") Tag tag);

    void deleteTags(@Param("ids") Collection<Integer> ids);
}
