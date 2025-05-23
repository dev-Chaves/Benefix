package com.hackaton.desafio.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@Tag(name = "API", description = "Endpoit for check if the API is online (health check).")
public class DemoController {

    @GetMapping("health")
    public String health(){
        return "API running on"+ System.getenv("HOSTNAME");
    }


}
