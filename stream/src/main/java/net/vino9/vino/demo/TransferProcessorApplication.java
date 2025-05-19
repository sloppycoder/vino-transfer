package net.vino9.vino.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransferProcessorApplication {
    public static void main(String[] args) {
        System.setProperty("spring.cloud.function.definition", "process");
        org.springframework.boot.SpringApplication.run(TransferProcessorApplication.class, args);
    }
}
