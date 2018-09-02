package org.mib.rest.exception;

import lombok.Getter;
import lombok.Setter;

public class BaseException extends RuntimeException {
    @Getter
    @Setter
    private String status;

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(String status, String msg) {
        super(msg);
        this.status = status;
    }
}
