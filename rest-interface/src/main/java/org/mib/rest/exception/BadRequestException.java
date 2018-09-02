package org.mib.rest.exception;

/**
 * Created by dufei on 18/1/23.
 */
public class BadRequestException extends BaseException {

    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(String status, String msg) {
        super(status, msg);
    }
}
