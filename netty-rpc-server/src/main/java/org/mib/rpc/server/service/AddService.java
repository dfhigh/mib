package org.mib.rpc.server.service;

import lombok.extern.slf4j.Slf4j;
import org.mib.rpc.model.Request;
import org.mib.rpc.model.Response;

import static org.mib.common.validator.Validator.validateObjectNotNull;

@Slf4j
public class AddService {

    private static volatile AddService INSTANCE = null;

    private AddService() {}

    public static AddService getInstance() {
        if (INSTANCE == null) {
            synchronized (AddService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AddService();
                }
            }
        }
        return INSTANCE;
    }

    public Response add(Request request) {
        validateObjectNotNull(request, "request");
        int result = request.getX() + request.getY();
        Response response = new Response();
        response.setResult(result);
        log.info("added {} and {} is {}", request.getX(), request.getY(), result);
        return response;
    }
}
