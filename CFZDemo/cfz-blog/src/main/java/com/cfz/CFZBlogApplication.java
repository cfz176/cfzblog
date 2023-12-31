package com.cfz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableScheduling
@SpringBootApplication
@MapperScan("com.cfz.mapper")
@EnableSwagger2
public class  CFZBlogApplication {

    public static void main(String[] args){
        SpringApplication.run(CFZBlogApplication.class, args);
    }

}
