package com.hackaton.desafio.repository;

import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.entity.PartnershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnershipRepository extends JpaRepository<PartnershipEntity, Long> {

    boolean existsBySupplierEnterpriseAndConsumerEnterprise(EnterpriseEntity supplier, EnterpriseEntity consumer);

    List<PartnershipEntity> findByConsumerEnterprise(Long id);

}
