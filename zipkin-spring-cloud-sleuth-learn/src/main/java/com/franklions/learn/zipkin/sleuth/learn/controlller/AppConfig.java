package com.franklions.learn.zipkin.sleuth.learn.controlller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;

/**
 * @author flsh
 * @version 1.0
 * @description
 * @date 2018/3/16
 * @since Jdk 1.8
 */
@Configuration
public class AppConfig {

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean public Executor poolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        return executor;
    }
}
