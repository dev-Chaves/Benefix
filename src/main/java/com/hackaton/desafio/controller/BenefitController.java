package com.hackaton.desafio.controller;

import com.hackaton.desafio.repository.BenefitRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("teste/benefits")
public class BenefitsController {

    private final BenefitRepository benefitsRepository;

}
