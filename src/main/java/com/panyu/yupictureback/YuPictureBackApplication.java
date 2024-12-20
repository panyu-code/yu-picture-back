package com.panyu.yupictureback;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.panyu.yupictureback.mapper")
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
public class YuPictureBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(YuPictureBackApplication.class, args);
    }




}
