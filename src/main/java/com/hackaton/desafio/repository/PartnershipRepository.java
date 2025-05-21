package com.hackaton.desafio.repository;

import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.entity.PartnershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnershipRepository extends JpaRepository<PartnershipEntity, Long> {

    boolean existsBySupplierEnterpriseAndConsumerEnterprise(EnterpriseEntity supplier, EnterpriseEntity consumer);

    @Query("SELECT p FROM PartnershipEntity p WHERE p.consumerEnterprise.id = :id OR p.supplierEnterprise.id = :id")
    List<PartnershipEntity> findByEnterpriseId(@Param("id") Long id);


}
