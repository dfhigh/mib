package org.mib.db.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.mib.db.model.Document;
import org.mib.rest.model.ListElementRequest;

import java.util.Collection;
import java.util.List;

@Mapper
public interface DocumentMapper {

    @Insert("insert into document (project_id, folder_id, name, created_at, prop1, prop2, prop3, prop4, prop5, prop6, " +
            "prop7, prop8, prop9) values (#{document.projectId}, #{document.folderId}, #{document.name}, " +
            "#{document.createdAt}, #{document.strProp1}, #{document.strProp2}, #{document.strProp3}, #{document.strProp4}, " +
            "#{document.intProp5}, #{document.intProp6},  #{document.intProp7}, #{document.intProp8}, #{document.intProp9})")
    @Options(useGeneratedKeys = true, keyProperty = "document.id")
    void createDocument(@Param("document") Document document);

    Document getDocument(@Param("id") int id);

    long countOfDocuments(@Param("params") ListElementRequest ler);

    List<Document> listDocuments(@Param("params") ListElementRequest ler);

    void updateDocument(@Param("document") Document document);

    void deleteDocuments(@Param("ids") Collection<Integer> ids);
}
