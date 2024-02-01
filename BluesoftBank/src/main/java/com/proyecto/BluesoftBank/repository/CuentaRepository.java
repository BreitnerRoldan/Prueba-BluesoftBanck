package com.proyecto.BluesoftBank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto.BluesoftBank.entity.Cuenta;


@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
	
	Optional<Cuenta> findById(Long id);
	
	@Query("SELECT c FROM Cuenta c WHERE c.cliente.id = :clienteId")
    List<Cuenta> findByClienteId(@Param("clienteId") Long clienteId);

}
