package org.mib.rest.exception;

/**
 * Created by dufei on 18/1/25.
 */
public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

    public ResourceNotFoundException(String status, String msg) {
        super(status, msg);
    }
}
