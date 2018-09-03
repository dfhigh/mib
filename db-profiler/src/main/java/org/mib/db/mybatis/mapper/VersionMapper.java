package org.mib.db.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.mib.db.model.Version;
import org.mib.rest.model.ListElementRequest;

import java.util.Collection;
import java.util.List;

@Mapper
public interface VersionMapper {

    @Insert("insert into version (project_id, folder_id, document_id, version, created_at, prop1, prop2, prop3, prop4, " +
            "prop5, prop6, prop7, prop8, prop9, prop10, prop11, prop12, prop13, prop14) values (#{version.projectId}, " +
            "#{version.folderId}, #{version.documentId}, #{version.version}, #{version.createdAt}, #{version.strProp1}, " +
            "#{version.strProp2}, #{version.strProp3}, #{version.strProp4}, #{version.strProp5}, #{version.strProp6}, " +
            "#{version.intProp7}, #{version.intProp8}, #{version.intProp9}, #{version.intProp10}, #{version.intProp11}, " +
            "#{version.intProp12}, #{version.intProp13}, #{version.intProp14})")
    @Options(useGeneratedKeys = true, keyProperty = "version.id")
    void createVersion(@Param("version") Version version);

    Version getVersion(@Param("id") int id);

    long countOfVersions(@Param("params") ListElementRequest ler);

    List<Version> listVersions(@Param("params") ListElementRequest ler);

    void updateVersion(@Param("version") Version version);

    void deleteVersions(@Param("ids") Collection<Integer> ids);
}
