package com.proyecto.BluesoftBank.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.BluesoftBank.Exception.CuentaNotFoundException;
import com.proyecto.BluesoftBank.Exception.SaldoNegativoException;
import com.proyecto.BluesoftBank.dto.InsertarSaldoRequest;
import com.proyecto.BluesoftBank.entity.Cuenta;
import com.proyecto.BluesoftBank.service.CuentaService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

	private final CuentaService cuentaService;

	public CuentaController(CuentaService cuentaService) {
		this.cuentaService = cuentaService;
	}

	@PostMapping("/insertarSaldo")
	public ResponseEntity<String> insertarSaldo(@RequestBody InsertarSaldoRequest request) {
		try {
			Long cuentaId = request.getCuentaId();
			double saldo = request.getMonto();

			cuentaService.insertarSaldo(cuentaId, saldo);

			return ResponseEntity.ok("Saldo insertado exitosamente.");
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta no encontrada: " + e.getMessage());
		} catch (SaldoNegativoException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al insertar el saldo: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
		}
	}

	@GetMapping("/{cuentaId}/saldo")
	public ResponseEntity<Double> consultarSaldo(@PathVariable Long cuentaId) {
		try {
			Double saldo = cuentaService.consultarSaldo(cuentaId);
			return new ResponseEntity<>(saldo, HttpStatus.OK);
		} catch (CuentaNotFoundException e) {
			// Manejo de cuenta no encontrada
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/crear-cuenta")
	public ResponseEntity<?> crearCuenta(@RequestBody Cuenta cuenta) {
		try {
			// Verifica y establece valores predeterminados para la cuenta en el servicio
			Cuenta cuentaGuardada = cuentaService.guardarCuenta(cuenta);

			// Devuelve una respuesta más detallada con los datos de la cuenta
			return ResponseEntity.ok(cuentaGuardada);
		} catch (CuentaNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta no encontrada: " + e.getMessage());
		} catch (DataIntegrityViolationException e) {
			// Manejar la excepción específica para violación de integridad de datos
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Error al insertar la cuenta con movimientos: " + e.getMessage());
		} catch (Exception e) {
			// Devuelve una respuesta de error más descriptiva
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error interno al procesar la solicitud: " + e.getMessage());
		}
	}

}