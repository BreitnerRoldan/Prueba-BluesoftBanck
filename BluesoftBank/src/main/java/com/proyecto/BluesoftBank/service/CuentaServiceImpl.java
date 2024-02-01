package com.proyecto.BluesoftBank.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.BluesoftBank.Exception.CuentaNotFoundException;
import com.proyecto.BluesoftBank.Exception.SaldoNegativoException;
import com.proyecto.BluesoftBank.entity.Cuenta;
import com.proyecto.BluesoftBank.entity.Movimiento;
import com.proyecto.BluesoftBank.repository.CuentaRepository;
import com.proyecto.BluesoftBank.repository.MovimientoRepository;

@Service
@Transactional
public class CuentaServiceImpl implements CuentaService {

	private final CuentaRepository cuentaRepository;
	private final MovimientoRepository movimientoRepository;

	public CuentaServiceImpl(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
		this.cuentaRepository = cuentaRepository;
		this.movimientoRepository = movimientoRepository;
	}

	@Override
	public void insertarSaldo(Long cuentaId, double monto) throws CuentaNotFoundException, SaldoNegativoException {
		Optional<Cuenta> optionalCuenta = cuentaRepository.findById(cuentaId);

		Cuenta cuenta = optionalCuenta.orElseGet(() -> {
			// Si la cuenta no existe, se crea
			Cuenta nuevaCuenta = new Cuenta();
			nuevaCuenta.setId(cuentaId);
			nuevaCuenta.setSaldo(0.0);
			
			return cuentaRepository.save(nuevaCuenta);
		});

		double saldoActual = cuenta.getSaldo();
		double nuevoSaldo = saldoActual + monto;

		if (nuevoSaldo < 0) {
			throw new SaldoNegativoException("La cuenta no puede tener un saldo negativo.");
		}

		cuenta.setSaldo(nuevoSaldo);
		cuentaRepository.save(cuenta);
	}

	@Override
	public Double consultarSaldo(Long cuentaId) throws CuentaNotFoundException {

		Optional<Cuenta> optionalCuenta = cuentaRepository.findById(cuentaId);

		if (optionalCuenta.isPresent()) {
			Cuenta cuenta = optionalCuenta.get();
			return cuenta.getSaldo();

		} else {
			// Manejo de cuenta no encontrada
			throw new CuentaNotFoundException("No se encontró la cuenta con ID: " + cuentaId);
		}
	}

	@Override
	public void save(Cuenta cuenta) {
		cuentaRepository.save(cuenta);

	}

	@Override
	public Cuenta guardarCuenta(Cuenta cuenta) throws CuentaNotFoundException {
		try {
	
			if (cuenta.getNumeroCuenta() == null || cuenta.getNumeroCuenta().isEmpty()) {

				cuenta.setNumeroCuenta(generarNumeroCuentaUnico());
			}

			if (cuenta.getTipoCuenta() == null || cuenta.getTipoCuenta().isEmpty()) {
				cuenta.setTipoCuenta("Cuenta Ahorros");
			}

			if (cuenta.getSaldo() < 0) {

				cuenta.setSaldo(0.0);
			}

			// Guarda la cuenta
			Cuenta cuentaGuardada = cuentaRepository.save(cuenta);

			// Asocia la cuenta a cada movimiento y guarda los movimientos
			if (cuenta.getMovimientos() != null) {
				for (Movimiento movimiento : cuenta.getMovimientos()) {
					movimiento.setCuenta(cuentaGuardada);
					movimientoRepository.save(movimiento);
				}
			}

			return cuentaGuardada;
		} catch (DataIntegrityViolationException e) {
			throw new CuentaNotFoundException(
					"No se pudo guardar la cuenta con movimientos. Detalles: " + e.getMessage());
		}
	}

	private String generarNumeroCuentaUnico() {
		
		String prefijo = "Cuenta";

		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String fechaActual = formatoFecha.format(new Date());

		// Combina el prefijo y la fecha actual para obtener un número de cuenta único
		return prefijo + fechaActual;
	}

}
