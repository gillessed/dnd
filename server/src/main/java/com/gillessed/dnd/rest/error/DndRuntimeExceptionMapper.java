package com.gillessed.dnd.rest.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class DndRuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    private static final Logger log = LoggerFactory.getLogger(DndRuntimeExceptionMapper.class);

    @Override
    public Response toResponse(RuntimeException exception) {
        log.warn("Server runtime exception. ", exception);
        if (exception instanceof DndException) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(((DndException) exception).getError())
                    .build();
        } else {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(DndError.Type.INTERNAL_SERVER_ERROR.error())
                    .build();
        }
    }
}
