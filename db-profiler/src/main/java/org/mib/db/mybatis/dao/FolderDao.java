package org.mib.db.mybatis.dao;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mib.db.model.Folder;
import org.mib.db.mybatis.mapper.FolderMapper;
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
public class FolderDao extends Dao {

    public FolderDao(final SqlSessionFactory ssf) {
        super(ssf);
    }

    public Folder createFolder(Folder folder) {
        validateObjectNotNull(folder, "folder");
        try (SqlSession session = ssf.openSession()) {
            FolderMapper mapper = session.getMapper(FolderMapper.class);
            mapper.createFolder(folder);
            return folder;
        }
    }

    public Folder getFolder(int id) {
        validateIntPositive(id, "folder id");
        try (SqlSession session = ssf.openSession()) {
            FolderMapper mapper = session.getMapper(FolderMapper.class);
            Folder folder = mapper.getFolder(id);
            if (folder == null) throw new ResourceNotFoundException("no folder found for " + id);
            return folder;
        }
    }

    public ListPayload<Folder> listFolders(int projectId, ListElementRequest ler) {
        validateIntPositive(projectId, "project id");
        validateObjectNotNull(ler, "list config");
        validateLongNotNegative(ler.getOffset(), "list offset");
        validateLongNotNegative(ler.getLimit(), "list limit");
        if (ler.getParams() == null) ler.setParams(Maps.newHashMapWithExpectedSize(1));
        ler.getParams().put("project_id", projectId);
        try (SqlSession session = ssf.openSession()) {
            FolderMapper mapper = session.getMapper(FolderMapper.class);
            long total = mapper.countOfFolders(ler);
            List<Folder> folders = mapper.listFolders(ler);
            return ListPayload.<Folder>builder().list(folders).total(total).offset(ler.getOffset()).build();
        }
    }

    public Folder updateFolder(Folder folder) {
        validateObjectNotNull(folder, "folder");
        validateIntPositive(folder.getId(), "folder id");
        try (SqlSession session = ssf.openSession()) {
            FolderMapper mapper = session.getMapper(FolderMapper.class);
            mapper.updateFolder(folder);
            return folder;
        }
    }

    public void deleteFolders(Collection<Integer> ids) {
        validateCollectionNotEmptyContainsNoNull(ids, "folder ids");
        try (SqlSession session = ssf.openSession()) {
            FolderMapper mapper = session.getMapper(FolderMapper.class);
            mapper.deleteFolders(ids);
        }
    }
}
