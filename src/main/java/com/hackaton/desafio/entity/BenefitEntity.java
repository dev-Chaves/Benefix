package com.hackaton.desafio.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_benefit")
public class BenefitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_enterprise_id", nullable = false)
    private EnterpriseEntity supplierEnterprise;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EnterpriseEntity getSupplierEnterprise() {
        return supplierEnterprise;
    }

    public void setSupplierEnterprise(EnterpriseEntity supplierEnterprise) {
        this.supplierEnterprise = supplierEnterprise;
    }
}
