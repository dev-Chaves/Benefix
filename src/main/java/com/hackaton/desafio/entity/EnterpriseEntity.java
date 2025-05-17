package com.hackaton.desafio.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_enterprise")
public class EnterpriseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enterprise", nullable = false, unique = true)
    private String enterprise;

    @Column(name = "cnpj", nullable = false, unique = true)
    private String cnpj;

    @OneToMany(mappedBy = "enterprise", cascade = CascadeType.ALL)
    private List<UserEntity> users;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

}
