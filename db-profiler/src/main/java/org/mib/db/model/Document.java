package org.mib.db.model;

import lombok.Data;

import java.util.Date;

@Data
public class Document {
    private int id;
    private int projectId;
    private int folderId;
    private String name;
    private Date createdAt;
    private String strProp1;
    private String strProp2;
    private String strProp3;
    private String strProp4;
    private int intProp5;
    private int intProp6;
    private int intProp7;
    private int intProp8;
    private int intProp9;
}
