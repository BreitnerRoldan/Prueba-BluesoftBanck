package com.proyecto.BluesoftBank.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.BluesoftBank.dto.ClienteRetirosDTO;
import com.proyecto.BluesoftBank.dto.ClienteTransaccionesDTO;
import com.proyecto.BluesoftBank.entity.Cliente;
import com.proyecto.BluesoftBank.entity.Cuenta;
import com.proyecto.BluesoftBank.entity.Movimiento;
import com.proyecto.BluesoftBank.repository.ClienteRepository;
import com.proyecto.BluesoftBank.repository.MovimientoRepository;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

	private final ClienteRepository clienteRepository;
	private final MovimientoRepository movimientoRepository;

	public ClienteServiceImpl(ClienteRepository clienteRepository, MovimientoRepository movimientoRepository) {
		this.clienteRepository = clienteRepository;
		this.movimientoRepository = movimientoRepository;
	}

	@Override
	public Cliente findById(Long id) {
		// TODO Auto-generated method stub
		return clienteRepository.findById(id).orElse(null);
	}

	@Override
	public List<Cliente> getAllClientes() {
		// TODO Auto-generated method stub
		return clienteRepository.findAll();
	}

	@Override
	public List<ClienteTransaccionesDTO> obtenerClientesConTransaccionesPorMes(int year, int month) {
	    // Obtiene la fecha de inicio y fin para el mes específico
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(year, month - 1, 1, 0, 0, 0);
	    Date startDate = calendar.getTime();
	    calendar.add(Calendar.MONTH, 1);
	    calendar.add(Calendar.DATE, -1);
	    Date endDate = calendar.getTime();

	    // Obtiene las transacciones para el mes específico
	    List<Movimiento> movimientos = movimientoRepository.findByFechaBetween(startDate, endDate);

	    // Crea un mapa para almacenar el recuento de transacciones por cliente
	    Map<Cliente, Integer> transaccionesPorCliente = new HashMap<>();

	    // Cuenta las transacciones por cliente
	    for (Movimiento movimiento : movimientos) {
	        Cuenta cuenta = movimiento.getCuenta();
	        Cliente cliente = cuenta.getCliente();
	        transaccionesPorCliente.put(cliente, transaccionesPorCliente.getOrDefault(cliente, 0) + 1);
	    }

	    // Crea una lista de DTO con información de cliente y transacciones
	    List<ClienteTransaccionesDTO> listaDTO = new ArrayList<>();
	    for (Map.Entry<Cliente, Integer> entry : transaccionesPorCliente.entrySet()) {
	        Cliente cliente = entry.getKey();
	        Integer numeroTransacciones = entry.getValue();
	        listaDTO.add(new ClienteTransaccionesDTO(cliente, numeroTransacciones));
	    }

	    // Ordena la lista descendentemente por el número de transacciones
	    listaDTO.sort(Comparator.comparingInt(ClienteTransaccionesDTO::getNumeroTransacciones).reversed());

	    return listaDTO;
	}

	public Cliente saveCliente(Cliente cliente) {
		return clienteRepository.save(cliente);
	}
	
	public List<ClienteRetirosDTO> obtenerClientesConRetirosFueraDeCiudad() {
	    // Obtener todos los movimientos
	    List<Movimiento> movimientos = movimientoRepository.findAll();

	    // Mapa para almacenar el total de retiros por cliente
	    Map<Cliente, Double> totalRetirosPorCliente = new HashMap<>();

	    // Iterar sobre los movimientos
	    for (Movimiento movimiento : movimientos) {
	        Cuenta cuenta = movimiento.getCuenta();
	        if (cuenta != null) {
	            Cliente cliente = cuenta.getCliente();
	            if (cliente != null) {
	                double monto = movimiento.getMonto();

	                // Verifica si el retiro es fuera de la ciudad de origen
	                if (!cliente.getCiudadOrigen().equals(cuenta.getCliente().getCiudadOrigen())) {
	                    // Actualiza el total de retiros para el cliente
	                    totalRetirosPorCliente.put(cliente, totalRetirosPorCliente.getOrDefault(cliente, 0.0) + monto);
	                }
	            }
	        }
	    }

	 // Filtra los clientes con un total de retiros superior a $1,000,000
	    List<ClienteRetirosDTO> clientesConRetirosSuperiores = totalRetirosPorCliente.entrySet().stream()
	            .filter(entry -> entry.getValue() > 1000000.0)
	            .map(entry -> new ClienteRetirosDTO(entry.getKey(), entry.getValue()))
	            .collect(Collectors.toList());

	    // Ordena la lista descendentemente por el total de retiros
	    clientesConRetirosSuperiores.sort(Comparator.comparingDouble(ClienteRetirosDTO::getTotalRetiros).reversed());

	    return clientesConRetirosSuperiores;
	}
}
