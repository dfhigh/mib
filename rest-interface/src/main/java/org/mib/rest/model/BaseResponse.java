package org.mib.rest.model;

import lombok.Data;

/**
 * Created by dufei on 17/11/4.
 */
@Data
public class BaseResponse {

    private Status baseStatus;
    private String status = "0";
    private String msg;

    public BaseResponse() {
        this.baseStatus = Status.OK;
    }

    /**
     * OK: 200
     * BAD_REQUEST: 400
     * UNAUTHORIZED: 401
     * FORBIDDEN: 403
     * NOT_FOUND: 404
     * INTERNAL_ERROR: 500
     */
    public enum Status {
        OK,
        BAD_REQUEST,
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        INTERNAL_ERROR
    }
}
