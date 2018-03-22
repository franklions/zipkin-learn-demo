package zipkin.brave.learn.demo.controller;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author flsh
 * @version 1.0
 * @description
 * @date 2018/3/20
 * @since Jdk 1.8
 */
@RestController(value="service2")
public class Service2Controller {
    @Autowired
    private OkHttpClient client;

    @GetMapping(value = "serviceA")
    public String serviceA() throws IOException, InterruptedException {
        Thread.sleep(1000);
        Request request = new Request.Builder()
                .url("http://localhost:8080/service1/serviceB")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return  response.body().string();
    }

    @GetMapping(value = "serviceB")
    public String serviceB(){
        return "service B";
    }

    @GetMapping(value = "serviceC")
    public String serviceC() throws InterruptedException, IOException {
        Thread.sleep(1000);
        Request request = new Request.Builder()
                .url("http://localhost:8080/service1/serviceB")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return  response.body().string();
    }
}
