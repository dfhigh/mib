package org.mib.rest.client.model;

import lombok.Data;

/**
 * Created by dufei on 18/5/8.
 */
@Data
public class ErrorResponse {
    private int errno;
    private String msg;
    private String error;
    private String status;
}
