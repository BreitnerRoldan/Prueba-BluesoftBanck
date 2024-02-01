package com.proyecto.BluesoftBank.dto;

import com.proyecto.BluesoftBank.entity.Cliente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ClienteRetirosDTO {
	
	private Cliente cliente;
    private double totalRetiros;

}
