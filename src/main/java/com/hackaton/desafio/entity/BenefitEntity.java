package com.hackaton.desafio.entity;

import com.hackaton.desafio.entity.Enum.BenefitCategory;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_benefit")
public class BenefitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BenefitCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_enterprise_id", nullable = false)
    private EnterpriseEntity supplierEnterprise;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public BenefitEntity() {
    }

    public BenefitEntity(String benefit, EnterpriseEntity enterprise, BenefitCategory category) {
        this.description = benefit;
        this.category = category;
        this.supplierEnterprise = enterprise;
        this.createdAt = LocalDateTime.now();
    }

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

    public BenefitCategory getCategory() {
        return category;
    }

    public void setCategory(BenefitCategory category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
