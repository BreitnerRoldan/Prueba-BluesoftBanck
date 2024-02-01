package com.proyecto.BluesoftBank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.BluesoftBank.entity.Extracto;

@Repository
public interface ExtractoRepository extends JpaRepository<Extracto, Long> {
    
	
	List<Extracto> findByCuentaIdAndMes(Long cuentaId, int mes);
}