package com.proyecto.BluesoftBank.service;

import java.util.List;

import com.proyecto.BluesoftBank.entity.Movimiento;

public interface MovimientoService {
	
	List<Movimiento> getMovimientosRecientes(Long cuentaId, int cantidadMovimientos);
	
	List<Movimiento> getMovimientosByMonth(Long cuentaId, int year, int month);
	

}
