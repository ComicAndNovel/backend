package com.example.backend.controller;

import com.example.backend.entity.RestBean;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
//    @ExceptionHandler(Exception.class)
//    public RestBean<String> error(Exception e) {
//        if (e instanceof NoHandlerFoundException exception) { // 处理 404
//            return RestBean.error(404, e.getMessage());
//        } else if (e instanceof ServletException exception) { // 其他 servlet异常返回 400
//            return RestBean.error(400, e.getMessage());
//        } else {
//            return RestBean.error(500, e.getMessage());
//        }
//    }
    @ExceptionHandler(value = Exception.class)
    public RestBean ExceptionHandler1(Exception e) {
        String message = "出现异常";

        if (e.getCause() != null && e.getCause().getMessage() != null) {
            message = e.getCause().getMessage();
        } else if (e.getMessage() != null) {
            message = e.getMessage();
        }
        log.error(message);


        return RestBean.error(0, message);
    }


}
