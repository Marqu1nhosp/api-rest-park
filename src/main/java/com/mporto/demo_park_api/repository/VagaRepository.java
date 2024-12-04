package com.mporto.demo_park_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mporto.demo_park_api.entity.Vaga;
import com.mporto.demo_park_api.entity.Vaga.StatusVaga;

public interface VagaRepository extends JpaRepository<Vaga, Long> {
    Optional<Vaga> findByCodigo(String codigo);

    Optional<Vaga> findFirstByStatus(StatusVaga livre);

}
