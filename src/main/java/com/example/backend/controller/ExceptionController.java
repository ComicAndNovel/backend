package com.example.backend.controller;

import com.example.backend.entity.RestBean;
import jakarta.servlet.ServletException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(Exception.class)
    public RestBean<String> error(Exception e) {
        if (e instanceof NoHandlerFoundException exception) { // 处理 404
            return RestBean.error(404, e.getMessage());
        } else if (e instanceof ServletException exception) { // 其他 servlet异常返回 400
            return RestBean.error(400, e.getMessage());
        } else {
            return RestBean.error(500, e.getMessage());
        }
    }
}
