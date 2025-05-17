package com.hackaton.desafio.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_benefit")
public class Beneficio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_fornecedora", nullable = false)
    private EnterpriseEntity supplierEnterprise;

}
