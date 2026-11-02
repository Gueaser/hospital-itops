package com.ye.hospitalitops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.ye.hospitalitops.mapper")
public class HospitalItopsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalItopsApplication.class, args);
    }

}
