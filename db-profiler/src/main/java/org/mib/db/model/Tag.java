package org.mib.db.model;

import lombok.Data;

import java.util.Date;

@Data
public class Tag {
    private int id;
    private String name;
    private String description;
    private Date createdAt;
}
