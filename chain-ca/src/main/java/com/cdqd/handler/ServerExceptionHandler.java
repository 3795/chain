package com.cdqd.handler;

import com.cdqd.exception.ServerException;
import com.cdqd.vo.ServerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ServerExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(ServerExceptionHandler.class);

    @ExceptionHandler(ServerException.class)
    @ResponseStatus(HttpStatus.OK)
    public ServerResponseVO handleServerException(ServerException se) {
        logger.warn("业务异常，Code: {}, ErrMessage: {}", se.getCode(), se.getMessage());
        return ServerResponseVO.error(se);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServerResponseVO handleException(Exception e) {
        e.printStackTrace();
        logger.error("系统异常， ErrMessage: " + e);
        return ServerResponseVO.error("服务器内部异常: " + e.getMessage());
    }
}
