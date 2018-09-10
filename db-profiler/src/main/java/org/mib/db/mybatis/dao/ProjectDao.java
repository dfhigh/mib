package org.mib.db.mybatis.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mib.db.model.Project;
import org.mib.db.mybatis.mapper.ProjectMapper;
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
public class ProjectDao extends Dao {

    public ProjectDao(final SqlSessionFactory ssf) {
        super(ssf);
    }

    public Project createProject(Project project) {
        validateObjectNotNull(project, "project");
        try (SqlSession session = ssf.openSession()) {
            ProjectMapper mapper = session.getMapper(ProjectMapper.class);
            mapper.createProject(project);
            return project;
        }
    }

    public Project getProject(int id) {
        validateIntPositive(id, "project id");
        try (SqlSession session = ssf.openSession()) {
            ProjectMapper mapper = session.getMapper(ProjectMapper.class);
            Project project = mapper.getProject(id);
            if (project == null) throw new ResourceNotFoundException("no project found for " + id);
            return project;
        }
    }

    public ListPayload<Project> listProjects(ListElementRequest ler) {
        validateObjectNotNull(ler, "list config");
        validateLongNotNegative(ler.getOffset(), "list offset");
        validateLongNotNegative(ler.getLimit(), "list limit");
        try (SqlSession session = ssf.openSession()) {
            ProjectMapper mapper = session.getMapper(ProjectMapper.class);
            long total = mapper.countOfProjects(ler);
            List<Project> projects = mapper.listProjects(ler);
            return ListPayload.<Project>builder().list(projects).total(total).offset(ler.getOffset()).build();
        }
    }

    public Project updateProject(Project project) {
        validateObjectNotNull(project, "project");
        validateIntPositive(project.getId(), "project id");
        try (SqlSession session = ssf.openSession()) {
            ProjectMapper mapper = session.getMapper(ProjectMapper.class);
            mapper.updateProject(project);
            return project;
        }
    }

    public void deleteProjects(Collection<Integer> ids) {
        validateCollectionNotEmptyContainsNoNull(ids, "project ids");
        try (SqlSession session = ssf.openSession()) {
            ProjectMapper mapper = session.getMapper(ProjectMapper.class);
            mapper.deleteProjects(ids);
        }
    }
}
