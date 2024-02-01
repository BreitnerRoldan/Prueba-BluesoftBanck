package com.proyecto.BluesoftBank.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.BluesoftBank.Exception.CuentaNotFoundException;
import com.proyecto.BluesoftBank.entity.Movimiento;
import com.proyecto.BluesoftBank.service.MovimientoService;

	@RestController
	@RequestMapping("/api/movimientos")
	public class MovimientoController {
	    private final MovimientoService movimientoService;

	    public MovimientoController(MovimientoService movimientoService) {
	        this.movimientoService = movimientoService;
	    }

	    @GetMapping("/cuenta/{cuentaId}/recientes")
	    public ResponseEntity<Map<String, Object>> getCuentaMovimientosRecientes(
	            @PathVariable Long cuentaId,
	            @RequestParam(defaultValue = "10") int cantidadMovimientos) {
	        try {
	            List<Movimiento> movimientos = movimientoService.getMovimientosRecientes(cuentaId, cantidadMovimientos);

	            // Crear un mapa para el cuerpo de la respuesta
	            Map<String, Object> responseBody = new HashMap<>();
	            responseBody.put("movimientos", movimientos);

	            // Agregar más información si es necesario
	            // responseBody.put("otraClave", otraInformacion);

	            // Devolver la respuesta con el cuerpo y el código de estado adecuado
	            return ResponseEntity.ok(responseBody);
	        } catch (CuentaNotFoundException e) {
	            // Manejar la excepción específica
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        } catch (Exception e) {
	            // Manejar otras excepciones de manera genérica
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }
	    
	    
	    @GetMapping("/by-month")
	    public ResponseEntity<List<Movimiento>> getMovimientosByMonth(
	            @RequestParam Long cuentaId,
	            @RequestParam int year,
	            @RequestParam int month) {

	        try {
	            List<Movimiento> movimientos = movimientoService.getMovimientosByMonth(cuentaId, year, month);
	            return ResponseEntity.ok(movimientos);
	        } catch (CuentaNotFoundException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }
	}