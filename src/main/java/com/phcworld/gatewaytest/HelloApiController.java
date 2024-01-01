package com.phcworld.gatewaytest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloApiController {

    @GetMapping(value = "/hello")
    public String helloGet(){
        return "Hello Get";
    }

    @GetMapping(value = "/microservice-hello")
    public String microserviceHelloGet(){
        return "This is Micro Service Hello Get";
    }
}
