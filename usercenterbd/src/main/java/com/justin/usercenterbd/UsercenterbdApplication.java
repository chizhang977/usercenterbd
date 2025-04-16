package com.justin.usercenterbd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.justin.usercenterbd.mapper")
public class UsercenterbdApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsercenterbdApplication.class, args);
	}

}
