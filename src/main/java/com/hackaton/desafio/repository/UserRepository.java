package com.hackaton.desafio.repository;

import com.hackaton.desafio.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
