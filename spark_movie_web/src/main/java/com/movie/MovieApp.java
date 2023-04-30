package com.movie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com/movie/mapper")
@SpringBootApplication
public class MovieApp {

    public static void main(String[] args) {
        SpringApplication.run(MovieApp.class, args);
        System.out.println("页面的访问地址：http://localhost:8080/");
    }
}