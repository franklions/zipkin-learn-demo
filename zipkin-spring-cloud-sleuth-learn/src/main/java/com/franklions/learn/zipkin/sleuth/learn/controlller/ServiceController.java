package com.franklions.learn.zipkin.sleuth.learn.controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;

/**
 * @author flsh
 * @version 1.0
 * @description
 * @date 2018/3/20
 * @since Jdk 1.8
 */
@RestController
public class ServiceController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AsyncRestTemplate traceAsyncRestTemplate;

    @Autowired @Qualifier("poolTaskExecutor")
    Executor executor ;

    @GetMapping(value = "serviceA")
    public String serviceA() throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return  restTemplate.exchange("http://localhost:8080/serviceB", HttpMethod.GET,
                    null,String.class).getBody();
        },executor);

        return future.get();
    }

    @GetMapping(value = "serviceB")
    public String serviceB(){
        return "service B";
    }

    @GetMapping(value = "serviceC")
    public String serviceC() throws ExecutionException, InterruptedException {
        return  this.traceAsyncRestTemplate.exchange("http://localhost:8080/serviceB", HttpMethod.GET,
                null,String.class).get().getBody();
    }

    @GetMapping(value = "serviceD")
    public String serviceD() throws ExecutionException, InterruptedException {
        return  this.restTemplate.exchange("http://localhost:8080/serviceC", HttpMethod.GET,
                null,String.class).getBody();
    }
}
