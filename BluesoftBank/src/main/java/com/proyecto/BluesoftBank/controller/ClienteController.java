package com.proyecto.BluesoftBank.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.BluesoftBank.dto.ClienteRetirosDTO;
import com.proyecto.BluesoftBank.dto.ClienteTransaccionesDTO;
import com.proyecto.BluesoftBank.entity.Cliente;
import com.proyecto.BluesoftBank.service.ClienteServiceImpl;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
	
	private final ClienteServiceImpl clienteService;
	
	public ClienteController(ClienteServiceImpl clienteService) {
        this.clienteService = clienteService;
    }
	
	@PostMapping("/guardar-cliente")
    public ResponseEntity<Cliente> saveCliente(@RequestBody Cliente cliente) {
        Cliente savedCliente = clienteService.saveCliente(cliente);
        return new ResponseEntity<>(savedCliente, HttpStatus.CREATED);
    }
	
	@GetMapping("/transacciones-por-mes")
    public ResponseEntity<List<ClienteTransaccionesDTO>> obtenerClientesConTransaccionesPorMes(
            @RequestParam int year,
            @RequestParam int month) {

        List<ClienteTransaccionesDTO> clientesConTransacciones = clienteService.obtenerClientesConTransaccionesPorMes(year, month);

        if (!clientesConTransacciones.isEmpty()) {
            return ResponseEntity.ok(clientesConTransacciones);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
	
	@GetMapping("/retiros-fuera-ciudad")
    public ResponseEntity<List<ClienteRetirosDTO>> obtenerClientesConRetirosFueraDeCiudad() {
        List<ClienteRetirosDTO> clientesRetiros = clienteService.obtenerClientesConRetirosFueraDeCiudad();

        if (!clientesRetiros.isEmpty()) {
            return ResponseEntity.ok(clientesRetiros);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
	