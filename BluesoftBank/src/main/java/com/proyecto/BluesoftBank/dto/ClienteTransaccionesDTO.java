package com.proyecto.BluesoftBank.dto;

import com.proyecto.BluesoftBank.entity.Cliente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ClienteTransaccionesDTO {
	
	private Cliente cliente;
    private int numeroTransacciones;

}
