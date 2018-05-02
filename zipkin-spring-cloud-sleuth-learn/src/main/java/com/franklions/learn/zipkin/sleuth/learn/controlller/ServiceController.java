package com.franklions.learn.zipkin.sleuth.learn.controlller;

import com.franklions.learn.zipkin.sleuth.learn.service.ValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

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
    Tracer tracer;

    @Autowired
    AsyncRestTemplate traceAsyncRestTemplate;

    @Autowired
    ValueService valueService;

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
        Span span =   this.tracer.getCurrentSpan();
        span.logEvent("cur begin");
        System.out.println("span========" + span.toString());
        Span beginSpan = this.tracer.createSpan("controller  begin span",span);
        this.tracer.addTag("taxValue", "abc tag");
        beginSpan.logEvent("controller begin");
        System.out.println("Controller begin span: " +beginSpan);

        String retval = valueService.getValue();

//        Span endSpan  = this.tracer.createSpan("controller  end span",tracer.getCurrentSpan());
//        System.out.println("Controller end span: " +endSpan);
        //关闭当前节点
        this.tracer.close(beginSpan);
        return retval;
    }

    @GetMapping(value = "serviceC")
    public String serviceC() throws ExecutionException, InterruptedException {

         Span span =   this.tracer.getCurrentSpan();
        System.out.println("span========" + span.toString());
        return  this.traceAsyncRestTemplate.exchange("http://localhost:8080/serviceB", HttpMethod.GET,
                null,String.class).get().getBody();
    }

    @GetMapping(value = "serviceD")
    public String serviceD() throws ExecutionException, InterruptedException {
        return  this.restTemplate.exchange("http://localhost:8080/serviceC", HttpMethod.GET,
                null,String.class).getBody();
    }

    @GetMapping("serviceE")
    public Callable<String> serviceE() {
        System.out.println("=====hello");
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(10L * 1000); //暂停两秒
                return "async api serviceE";
            }
        };
    }

    @GetMapping(value = "serviceF")
    public String serviceF() throws ExecutionException, InterruptedException {
        ListenableFuture<ResponseEntity<String>> future =
                traceAsyncRestTemplate.exchange("http://localhost:8090/serviceE",HttpMethod.GET,
                        null,String.class);

        future.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("======client failure : " + throwable);
            }

            @Override
            public void onSuccess(ResponseEntity<String> stringResponseEntity) {
                System.out.println("======client get result : " + stringResponseEntity.getBody());
            }
        });

        return future.get().getBody();
    }
}
