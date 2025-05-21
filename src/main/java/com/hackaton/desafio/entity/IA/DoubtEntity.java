package com.hackaton.desafio.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_doubt")
public class DoubtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private Boolean answered = false;

    private LocalDateTime createdAt;
    


}
