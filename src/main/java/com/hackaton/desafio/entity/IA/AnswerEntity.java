package com.hackaton.desafio.entity.IA;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_answer")
public class AnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "doubt_id")
    private DoubtEntity doubt;

    private String response;

    private LocalDateTime createdAt;


}
