package com.hackaton.desafio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class DemoController {

    @GetMapping("health")
    public String health(){
        return "API running on"+ System.getenv("HOSTNAME");
    }


}
