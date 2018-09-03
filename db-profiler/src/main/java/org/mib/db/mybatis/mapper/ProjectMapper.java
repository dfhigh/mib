package org.mib.db.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.mib.db.model.Project;
import org.mib.rest.model.ListElementRequest;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ProjectMapper {

    @Insert("insert into project (name, description, created_at) values (#{project.name}, " +
            "#{project.description}, #{project.createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "project.id")
    void createProject(@Param("project") Project project);

    Project getProject(@Param("id") int id);

    long countOfProjects(@Param("params") ListElementRequest ler);

    List<Project> listProjects(@Param("params") ListElementRequest ler);

    void updateProject(@Param("project") Project project);

    void deleteProjects(@Param("ids") Collection<Integer> ids);
}
