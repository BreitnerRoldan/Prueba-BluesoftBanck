package com.proyecto.BluesoftBank.service;

import java.util.List;

import com.proyecto.BluesoftBank.dto.ClienteTransaccionesDTO;
import com.proyecto.BluesoftBank.entity.Cliente;

public interface ClienteService {

	Cliente findById(Long id);

	List<Cliente> getAllClientes();
	
	List<ClienteTransaccionesDTO> obtenerClientesConTransaccionesPorMes(int year, int month);
	
	Cliente saveCliente(Cliente cliente);
	
}