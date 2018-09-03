package org.mib.db.model;

import lombok.Data;

import java.util.Date;

@Data
public class Folder {
    private int id;
    private int projectId;
    private String name;
    private String description;
    private Date createdAt;
}
