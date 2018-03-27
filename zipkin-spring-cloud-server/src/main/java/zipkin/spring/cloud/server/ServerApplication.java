package zipkin.spring.cloud.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.EnableZipkinServer;

/**
 * @author flsh
 * @version 1.0
 * @description
 * @date 2018/3/26
 * @since Jdk 1.8
 */
@SpringBootApplication
@EnableZipkinServer
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class,args);
    }
}
