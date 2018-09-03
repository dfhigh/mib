package org.mib.db.model;

import lombok.Data;

import java.util.Date;

@Data
public class Version {
    private int id;
    private int projectId;
    private int folderId;
    private int documentId;
    private String version;
    private Date createdAt;
    private String strProp1;
    private String strProp2;
    private String strProp3;
    private String strProp4;
    private String strProp5;
    private String strProp6;
    private int intProp7;
    private int intProp8;
    private int intProp9;
    private int intProp10;
    private int intProp11;
    private int intProp12;
    private int intProp13;
    private int intProp14;
}
