package io.github.victorhsr.ttxn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class Loader {

    public static void main(String[] args) {
        SpringApplication.run(Loader.class, args);
    }
}
