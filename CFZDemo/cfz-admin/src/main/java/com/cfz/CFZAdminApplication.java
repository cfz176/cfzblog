package com.cfz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cfz.mapper")
public class CFZAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(CFZAdminApplication.class, args);
    }
}
