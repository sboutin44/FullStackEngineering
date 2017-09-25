package com.seb.email.routing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.ws.WebEndpoint;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        /* Main entry point for a spring boot application */

        SpringApplication.run(Application.class, args);
    }

}