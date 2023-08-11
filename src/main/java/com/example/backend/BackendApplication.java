package com.example.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(
	exclude = {
		// 去掉 security 的登录页面
		SecurityAutoConfiguration.class
	}
)
@MapperScan("com.example")
public class BackendApplication {

	public static void main(String[] args) {


		SpringApplication.run(BackendApplication.class, args);
	}

}