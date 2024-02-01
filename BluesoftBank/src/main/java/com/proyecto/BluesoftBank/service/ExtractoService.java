package com.proyecto.BluesoftBank.service;

import java.util.List;

import com.proyecto.BluesoftBank.entity.Extracto;

public interface ExtractoService {

	List<Extracto> generarExtractoMensual(Long cuentaId, int mes);

}
