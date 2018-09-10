package org.mib.db.mybatis.dao;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mib.db.model.Document;
import org.mib.db.mybatis.mapper.DocumentMapper;
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
public class DocumentDao extends Dao {

    public DocumentDao(final SqlSessionFactory ssf) {
        super(ssf);
    }

    public Document createDocument(Document document) {
        validateObjectNotNull(document, "document");
        try (SqlSession session = ssf.openSession()) {
            DocumentMapper mapper = session.getMapper(DocumentMapper.class);
            mapper.createDocument(document);
            return document;
        }
    }

    public Document getDocument(int id) {
        validateIntPositive(id, "document id");
        try (SqlSession session = ssf.openSession()) {
            DocumentMapper mapper = session.getMapper(DocumentMapper.class);
            Document document = mapper.getDocument(id);
            if (document == null) throw new ResourceNotFoundException("no document found for " + id);
            return document;
        }
    }

    public ListPayload<Document> listDocuments(int folderId, ListElementRequest ler) {
        validateIntPositive(folderId, "folder id");
        validateObjectNotNull(ler, "list config");
        validateLongNotNegative(ler.getOffset(), "list offset");
        validateLongNotNegative(ler.getLimit(), "list limit");
        if (ler.getParams() == null) ler.setParams(Maps.newHashMapWithExpectedSize(1));
        ler.getParams().put("folder_id", folderId);
        try (SqlSession session = ssf.openSession()) {
            DocumentMapper mapper = session.getMapper(DocumentMapper.class);
            long total = mapper.countOfDocuments(ler);
            List<Document> documents = mapper.listDocuments(ler);
            return ListPayload.<Document>builder().list(documents).total(total).offset(ler.getOffset()).build();
        }
    }

    public Document updateDocument(Document document) {
        validateObjectNotNull(document, "document");
        validateIntPositive(document.getId(), "document id");
        try (SqlSession session = ssf.openSession()) {
            DocumentMapper mapper = session.getMapper(DocumentMapper.class);
            mapper.updateDocument(document);
            return document;
        }
    }

    public void deleteDocuments(Collection<Integer> ids) {
        validateCollectionNotEmptyContainsNoNull(ids, "document ids");
        try (SqlSession session = ssf.openSession()) {
            DocumentMapper mapper = session.getMapper(DocumentMapper.class);
            mapper.deleteDocuments(ids);
        }
    }
}
