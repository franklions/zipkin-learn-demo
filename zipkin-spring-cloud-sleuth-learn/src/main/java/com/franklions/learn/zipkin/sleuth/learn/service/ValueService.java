package com.franklions.learn.zipkin.sleuth.learn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;

/**
 * @author flsh
 * @version 1.0
 * @description
 * @date 2018/5/2
 * @since Jdk 1.8
 */
@Service
public class ValueService {

    @Autowired
    Tracer tracer;

    public String getValue(){
        Span curSpan  = tracer.getCurrentSpan();
        System.out.println("service cur span: " +curSpan.toString());

        Span continueSpan =  tracer.continueSpan(curSpan);
        continueSpan.logEvent("gatValue begin");
        System.out.println( "continue span" + continueSpan);

        Span endSpan   = tracer.createSpan("service getValue span" ,curSpan);
        endSpan.logEvent("getValue method begin ");
        System.out.println("Service end span: " +endSpan.toString());

        //延迟一秒钟
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        endSpan.logEvent("getValue method end ");
        tracer.close(tracer.getCurrentSpan());
        return "Service B";
    }
}
