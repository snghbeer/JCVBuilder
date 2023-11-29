package com.example.jcvbuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })

public class JcvBuilderApplication {

    public static void main(String[] args) {
        SpringApplication.run(JcvBuilderApplication.class, args);
    }

}
