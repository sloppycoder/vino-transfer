package net.vino9.vino.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransferAPIApplication {
    public static void main(String[] args) {
        System.setProperty("spring.cloud.function.definition", "submit;result");
        SpringApplication.run(TransferAPIApplication.class, args);
    }
}
