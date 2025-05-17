package com.hackaton.desafio.repository;

import com.hackaton.desafio.entity.BenefitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BenefitRepository extends JpaRepository<BenefitEntity, Long> {
}
