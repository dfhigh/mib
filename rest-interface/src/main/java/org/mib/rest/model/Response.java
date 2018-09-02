package org.mib.rest.model;

import lombok.Data;

/**
 * Created by dufei on 17/10/10.
 */
@Data
public class Response<T> extends BaseResponse {
    private T data;
}
