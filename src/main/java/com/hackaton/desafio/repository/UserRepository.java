package com.hackaton.desafio.repository;

import com.hackaton.desafio.entity.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByName(String name);

    @Query(value = "SELECT * FROM tb_user WHERE cpf = :cpf", nativeQuery = true)
    Optional<UserEntity> findByCpf(@Param("cpf") String cpf);
}
