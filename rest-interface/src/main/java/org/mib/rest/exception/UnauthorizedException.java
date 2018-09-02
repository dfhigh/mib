package org.mib.rest.exception;

/**
 * Created by dufei on 18/1/23.
 */
public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String msg) {
        super(msg);
    }

    public UnauthorizedException(String status, String msg) {
        super(status, msg);
    }
}