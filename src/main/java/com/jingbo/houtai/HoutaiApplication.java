package com.jingbo.houtai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.jingbo.houtai.dao"})
public class HoutaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoutaiApplication.class, args);
    }

}
