package org.mib.db.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.mib.db.model.Folder;
import org.mib.rest.model.ListElementRequest;

import java.util.Collection;
import java.util.List;

@Mapper
public interface FolderMapper {

    @Insert("insert into folder (project_id, name, description, created_at) values (#{folder.projectId}, " +
            "#{folder.name}, #{folder.description}, #{folder.createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "folder.id")
    void createFolder(@Param("folder") Folder folder);

    Folder getFolder(@Param("id") int id);

    long countOfFolders(@Param("params") ListElementRequest ler);

    List<Folder> listFolders(@Param("params") ListElementRequest ler);

    void updateFolder(@Param("folder") Folder folder);

    void deleteFolders(@Param("ids") Collection<Integer> ids);
}
