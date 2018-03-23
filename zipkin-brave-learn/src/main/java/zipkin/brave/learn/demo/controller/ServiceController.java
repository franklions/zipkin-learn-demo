package zipkin.brave.learn.demo.controller;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * @author flsh
 * @version 1.0
 * @description
 * @date 2018/3/16
 * @since Jdk 1.8
 */
@RestController(value="service1")
public class ServiceController {

    @Autowired
    private OkHttpClient client;

    @GetMapping(value = "serviceAA")
    public String serviceA() throws IOException, InterruptedException {
        Thread.sleep(1000);
        Request request = new Request.Builder()
                .url("http://localhost:8090/serviceB")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return  response.body().string();
    }

//    @GetMapping(value = "serviceB")
//    public String serviceB(){
//        return "service B";
//    }

    @GetMapping(value = "serviceAC")
    public String serviceC() throws InterruptedException, IOException {
        Thread.sleep(1000);
        Request request = new Request.Builder()
                .url("http://localhost:8090/serviceB")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return  response.body().string();
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
}
