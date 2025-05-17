package com.hackaton.desafio.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_empresa")
public class EmpresaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enterprise", nullable = false, unique = true)
    private String enterprise;

    @Column(name = "cnpj", nullable = false, unique = true)
    private String cnpj;

    @Column(name = created_at, nullable = false)

}
