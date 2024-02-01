package com.proyecto.BluesoftBank.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.proyecto.BluesoftBank.Exception.CuentaNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.BluesoftBank.entity.Cuenta;
import com.proyecto.BluesoftBank.entity.Movimiento;
import com.proyecto.BluesoftBank.repository.CuentaRepository;
import com.proyecto.BluesoftBank.repository.MovimientoRepository;

@Service
@Transactional
public class MovimientoServiceImpl implements MovimientoService {

	
	private static final Logger logger = LoggerFactory.getLogger(MovimientoServiceImpl.class);
	
	private final CuentaRepository cuentaRepository;
	private final MovimientoRepository movimientoRepository;

	public MovimientoServiceImpl(MovimientoRepository movimientoRepository, CuentaRepository cuentaRepository) {
		this.movimientoRepository = movimientoRepository;
		this.cuentaRepository = cuentaRepository;
	}

	@Override
	public List<Movimiento> getMovimientosRecientes(Long cuentaId, int cantidadMovimientos) {
	    try {
	        // Validar la existencia de la cuenta
	        Cuenta cuenta = cuentaRepository.findById(cuentaId)
	                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con ID: " + cuentaId));

	        // Obtener los movimientos recientes
	        List<Movimiento> movimientos = movimientoRepository.findTopNMovimientosRecientes(cuentaId, cantidadMovimientos);

	        // Registrar información relevante
	        logger.info("Obtenidos {} movimientos recientes para la cuenta {}", movimientos.size(), cuentaId);

	        return movimientos;
	    } catch (CuentaNotFoundException e) {
	        // Manejar la excepción específica
	        logger.error("Error al obtener movimientos recientes. {}", e.getMessage());
	        throw e;
	    } catch (Exception e) {
	        // Manejar otras excepciones de manera genérica
	        logger.error("Error inesperado al obtener movimientos recientes. {}", e.getMessage());
	        throw new RuntimeException("Error inesperado al obtener movimientos recientes", e);
	    }
	}

	@Override
	public List<Movimiento> getMovimientosByMonth(Long cuentaId, int year, int month) {
		// Obtener la cuenta
		Cuenta cuenta = cuentaRepository.findById(cuentaId)
				.orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con ID: " + cuentaId));

		// Calcular el rango de fechas para el mes específico
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1, 0, 0, 0);
		Date startDate = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		Date endDate = calendar.getTime();

		// Obtener los movimientos dentro del rango de fechas
		List<Movimiento> movimientos = movimientoRepository.findByCuentaAndFechaBetween(cuenta, startDate, endDate);

		// Calcular el saldo al final del mes
		double saldoFinal = calcularSaldoAlFinalDelMes(cuenta, endDate, movimientos);

		// se agrega un movimiento ficticio
		Movimiento saldoFinalMovimiento = new Movimiento();
		saldoFinalMovimiento.setId(cuentaId);
		saldoFinalMovimiento.setFecha(endDate);
		saldoFinalMovimiento.setTipoOperacion("Saldo Final");
		saldoFinalMovimiento.setMonto(saldoFinal);
		saldoFinalMovimiento.setCuenta(cuenta);
		movimientos.add(saldoFinalMovimiento);

		return movimientos;
	}

	public double calcularSaldoAlFinalDelMes(Cuenta cuenta, Date endDate, List<Movimiento> movimientos) {
		double saldo = cuenta.getSaldo();

        // Sumar los montos de los movimientos hasta la fecha de fin del mes
        for (Movimiento movimiento : movimientos) {
            // Asegúrate de que el movimiento esté dentro del rango del mes
            if (movimiento.getFecha().before(endDate) || movimiento.getFecha().equals(endDate)) {
                // Sumar o restar el monto según el tipo de operación
                if ("DEPOSITO".equals(movimiento.getTipoOperacion())) {
                    saldo += movimiento.getMonto();
                } else if ("RETIRO".equals(movimiento.getTipoOperacion())) {
                    saldo -= movimiento.getMonto();
                }
               
            }
        }

        return saldo;
	}
}
