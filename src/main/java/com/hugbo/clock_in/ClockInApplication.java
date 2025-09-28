package com.hugbo.clock_in;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ClockInApplication {
    public static void main(String[] args) {
    	SpringApplication.run(ClockInApplication.class, args);
    }
}
