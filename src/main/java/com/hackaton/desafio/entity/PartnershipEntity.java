package com.hackaton.desafio.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_partnership")
public class PartnershipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_enterprise_id", nullable = false)
    private EnterpriseEntity consumerEnterprise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_enterprise_id", nullable = false)
    private EnterpriseEntity supplierEnterprise;

}
