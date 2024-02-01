package com.proyecto.BluesoftBank.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.BluesoftBank.dto.SaldoPorDia;
import com.proyecto.BluesoftBank.entity.Extracto;
import com.proyecto.BluesoftBank.entity.Movimiento;
import com.proyecto.BluesoftBank.repository.ExtractoRepository;
import com.proyecto.BluesoftBank.repository.MovimientoRepository;

@Service
@Transactional
public class ExtractoServiceImpl implements ExtractoService {
	
	private final ExtractoRepository extractoRepository;
	private final MovimientoRepository movimientoRepository;
	
	public ExtractoServiceImpl(ExtractoRepository extractoRepository, MovimientoRepository movimientoRepository) {
        this.extractoRepository = extractoRepository;
        this.movimientoRepository=movimientoRepository;
    }

	@Override
	public List<Extracto> generarExtractoMensual(Long cuentaId, int mes) {
	    // Recupera movimientos para la cuenta y mes específicos
	    List<Movimiento> movimientos = movimientoRepository.findMovimientosPorCuentaYMes(cuentaId, mes);

	    // Calcula el saldo acumulado por día
	    List<SaldoPorDia> saldosPorDia = new ArrayList();
	    for (Movimiento movimiento : movimientos) {
	        int dia = movimiento.getFecha().getDay();
	        Optional<SaldoPorDia> saldoExistente = saldosPorDia.stream()
	                .filter(saldo -> saldo.getDia() == dia)
	                .findFirst();

	        if (saldoExistente.isPresent()) {
	            saldoExistente.get().setSaldoAcumulado(saldoExistente.get().getSaldoAcumulado() + movimiento.getMonto());
	        } else {
	            SaldoPorDia nuevoSaldo = new SaldoPorDia();
	            nuevoSaldo.setDia(dia);
	            nuevoSaldo.setSaldoAcumulado(movimiento.getMonto());
	            saldosPorDia.add(nuevoSaldo);
	        }
	    }

	    // Guarda el extracto mensual en la base de datos
	    Extracto extracto = new Extracto();
	    extracto.setId(cuentaId);
	    extracto.setMes(mes);
	    extracto.setSaldosPorDia(saldosPorDia);
	    extractoRepository.save(extracto);

	    // Devolver los extractos
	    return extractoRepository.findByCuentaIdAndMes(cuentaId, mes);
	}
}