package com.wanling.trigger.http;

import com.wanling.types.enums.ResponseCode;
import com.wanling.types.exception.AppException;
import com.wanling.types.model.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle known business exceptions
     */
    @ExceptionHandler(AppException.class)
    public Response<?> handleAppException(AppException e) {
        return Response.fail(e.getCode(), e.getInfo());
    }

    /**
     * Handle unknown/unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    public Response<?> handleUnknownException(Exception e) {
        e.printStackTrace(); // optional: log for diagnostics
        return Response.fail(ResponseCode.UNKNOWN_ERROR.getCode(), String.valueOf(ResponseCode.UNKNOWN_ERROR));
    }
}