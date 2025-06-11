package com.hackaton.desafio.repository;

import com.hackaton.desafio.entity.BenefitEntity;
import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.entity.Enum.BenefitCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenefitRepository extends JpaRepository<BenefitEntity, Long> {

    @Query("SELECT b FROM BenefitEntity b WHERE b.supplierEnterprise.id = :enterpriseId")
    List<BenefitEntity> findByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    List<BenefitEntity> findBySupplierEnterprise_IdIn(List<Long> enterpriseIds);

    List<BenefitEntity> findByCategory(BenefitCategory category);

    List<BenefitEntity> findByCategoryAndSupplierEnterprise(BenefitCategory category, EnterpriseEntity supplierEnterprise);

    List<BenefitEntity> findByCategoryAndSupplierEnterpriseIn(BenefitCategory category, List<EnterpriseEntity> enterprises);


}
