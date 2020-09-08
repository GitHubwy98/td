package com.wudi.td;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@SpringBootApplication
@ServletComponentScan(basePackages = {"com.wudi.td..filter"})
public class TdApplication {

    public static void main(String[] args) {
        SpringApplication.run(TdApplication.class, args);
    }

}
