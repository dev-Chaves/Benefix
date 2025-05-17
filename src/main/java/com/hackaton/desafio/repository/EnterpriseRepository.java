package com.hackaton.desafio.repository;

import com.hackaton.desafio.entity.EnterpriseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnterpriseRepository extends JpaRepository<EnterpriseEntity, Long> {
}
