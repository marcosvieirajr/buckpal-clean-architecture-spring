package br.com.mvj.buckpal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"br.com.mvj.buckpal.application.usecase"})
public class BuckpalApp {

    public static void main(String[] args) {
        SpringApplication.run(BuckpalApp.class, args);
    }

}
