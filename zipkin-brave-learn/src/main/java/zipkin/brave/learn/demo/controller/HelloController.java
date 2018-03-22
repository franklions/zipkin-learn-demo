package zipkin.brave.learn.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author flsh
 * @version 1.0
 * @description
 * @date 2018/3/16
 * @since Jdk 1.8
 */
@RestController
public class HelloController {
    @RequestMapping(value = "/say")
    public String sayHello(){
        return "Hello world";
    }
}
