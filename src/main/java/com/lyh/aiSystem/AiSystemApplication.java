package com.lyh.aiSystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.lyh.aiSystem.mapper")
@SpringBootApplication
public class AiSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiSystemApplication.class, args);
    }

}
