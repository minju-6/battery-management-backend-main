package wid.bmsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BmsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BmsBackendApplication.class, args);
    }

}
