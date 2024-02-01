package com.proyecto.BluesoftBank.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto.BluesoftBank.entity.Cuenta;
import com.proyecto.BluesoftBank.entity.Movimiento;


@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
	
	@Query("SELECT m FROM Movimiento m WHERE m.cuenta.id = :cuentaId ORDER BY m.fecha DESC LIMIT :cantidadMovimientos")
	List<Movimiento> findTopNMovimientosRecientes(Long cuentaId, int cantidadMovimientos);
	
	List<Movimiento> findByCuentaAndFechaBetween(Cuenta cuenta, Date startDate, Date endDate);
	
	List<Movimiento> findByFechaBetween( Date startDate, Date endDate);
	
	
	@Query("SELECT m FROM Movimiento m WHERE m.cuenta.id = :cuentaId AND MONTH(m.fecha) = :mes")
    List<Movimiento> findMovimientosPorCuentaYMes(
        @Param("cuentaId") Long cuentaId,
        @Param("mes") int mes
    );

}
