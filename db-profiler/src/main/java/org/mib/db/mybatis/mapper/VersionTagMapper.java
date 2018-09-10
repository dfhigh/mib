package org.mib.db.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mib.db.model.Tag;
import org.mib.db.model.Version;

import java.util.List;

@Mapper
public interface VersionTagMapper {

    @Insert("insert into tag_version (version_id, tag_id) values (#{versionId}, #{tagId})")
    void createVersionTag(@Param("versionId") int versionId, @Param("tagId") int tagId);

    long countOfTaggedVersions(@Param("tagId") int tagId);

    List<Version> listVersionsByTag(@Param("tagId") int tagId, @Param("limit") int limit, @Param("offset") long offset);

    long countOfVersionTags(@Param("versionId") int versionId);

    List<Tag> listTagsByVersion(@Param("versionId") int versionId, @Param("limit") int limit, @Param("offset") long offset);

    void deleteVersionTag(@Param("versionId") int versionId, @Param("tagId") int tagId);

    void deleteTag(@Param("tagId") int tagId);
}
