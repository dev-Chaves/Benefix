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

    public PartnershipEntity() {

    }

    public PartnershipEntity(EnterpriseEntity empresa1, EnterpriseEntity empresa2) {
        if (empresa1.getId() < empresa2.getId()) {
            this.consumerEnterprise = empresa1;
            this.supplierEnterprise = empresa2;
        } else {
            this.consumerEnterprise = empresa2;
            this.supplierEnterprise = empresa1;
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnterpriseEntity getConsumerEnterprise() {
        return consumerEnterprise;
    }

    public void setConsumerEnterprise(EnterpriseEntity consumerEnterprise) {
        this.consumerEnterprise = consumerEnterprise;
    }

    public EnterpriseEntity getSupplierEnterprise() {
        return supplierEnterprise;
    }

    public void setSupplierEnterprise(EnterpriseEntity supplierEnterprise) {
        this.supplierEnterprise = supplierEnterprise;
    }
}
