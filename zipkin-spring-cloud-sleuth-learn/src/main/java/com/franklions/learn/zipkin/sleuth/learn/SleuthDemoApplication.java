package com.franklions.learn.zipkin.sleuth.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author flsh
 * @version 1.0
 * @description
 * @date 2018/3/20
 * @since Jdk 1.8
 */
@SpringBootApplication
public class SleuthDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SleuthDemoApplication.class,args);
    }
}
