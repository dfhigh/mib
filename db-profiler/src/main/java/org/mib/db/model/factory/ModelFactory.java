package org.mib.db.model.factory;

import lombok.extern.slf4j.Slf4j;
import org.mib.db.model.Document;
import org.mib.db.model.Folder;
import org.mib.db.model.Project;
import org.mib.db.model.Tag;
import org.mib.db.model.Version;
import org.mib.db.model.VersionTag;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Slf4j
public class ModelFactory {

    public static Project randomProject() {
        Project project = new Project();
        project.setName("p-" + randomAlphanumeric(6));
        project.setDescription(randomAlphanumeric(50, 200));
        project.setCreatedAt(new Date());
        return project;
    }

    public static Folder randomFolder(int projectId) {
        Folder folder = new Folder();
        folder.setName(randomAlphanumeric(1, 64));
        folder.setDescription(randomAlphanumeric(50, 200));
        folder.setProjectId(projectId);
        folder.setCreatedAt(new Date());
        return folder;
    }

    public static Document randomDocument(int projectId, int folderId) {
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        Document doc = new Document();
        doc.setProjectId(projectId);
        doc.setFolderId(folderId);
        doc.setCreatedAt(new Date());
        doc.setStrProp1(randomAlphanumeric(1, 256));
        doc.setStrProp2(randomAlphanumeric(1, 256));
        doc.setStrProp3(randomAlphanumeric(1, 256));
        doc.setStrProp4(randomAlphanumeric(1, 256));
        doc.setIntProp5(tlr.nextInt());
        doc.setIntProp6(tlr.nextInt());
        doc.setIntProp7(tlr.nextInt());
        doc.setIntProp8(tlr.nextInt());
        doc.setIntProp9(tlr.nextInt());
        return doc;
    }

    public static Version randomVersion(int projectId, int folderId, int docId) {
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        Version version = new Version();
        version.setProjectId(projectId);
        version.setFolderId(folderId);
        version.setDocumentId(docId);
        version.setCreatedAt(new Date());
        version.setStrProp1(randomAlphanumeric(1, 256));
        version.setStrProp2(randomAlphanumeric(1, 256));
        version.setStrProp3(randomAlphanumeric(1, 256));
        version.setStrProp4(randomAlphanumeric(1, 256));
        version.setStrProp5(randomAlphanumeric(1, 256));
        version.setStrProp6(randomAlphanumeric(1, 256));
        version.setIntProp7(tlr.nextInt());
        version.setIntProp8(tlr.nextInt());
        version.setIntProp9(tlr.nextInt());
        version.setIntProp10(tlr.nextInt());
        version.setIntProp11(tlr.nextInt());
        version.setIntProp12(tlr.nextInt());
        version.setIntProp13(tlr.nextInt());
        version.setIntProp14(tlr.nextInt());
        return version;
    }

    public static Tag randomTag() {
        Tag tag = new Tag();
        tag.setName("t-" + randomAlphanumeric(8));
        tag.setDescription(randomAlphanumeric(1, 256));
        tag.setCreatedAt(new Date());
        return tag;
    }

    public static VersionTag versionTag(int versionId, int tagId) {
        return new VersionTag(versionId, tagId);
    }

}
