package org.mib.rest.exception;

public class ForbiddenException extends BaseException {

    public ForbiddenException(String msg) {
        super(msg);
    }

    public ForbiddenException(String status, String msg) {
        super(status, msg);
    }
}
