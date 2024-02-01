package com.proyecto.BluesoftBank.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.BluesoftBank.entity.Extracto;
import com.proyecto.BluesoftBank.service.ExtractoService;

@RestController
@RequestMapping("/api/extractos")
public class ExtractoController {
	private final ExtractoService extractoService;

	public ExtractoController(ExtractoService extractoService) {
		this.extractoService = extractoService;
	}

	@GetMapping("/generar-mensual")
    public ResponseEntity<List<Extracto>> generarExtractoMensual(
            @RequestParam Long cuentaId,
            @RequestParam int mes) {

        List<Extracto> extractosGenerados = extractoService.generarExtractoMensual(cuentaId, mes);

        if (extractosGenerados != null && !extractosGenerados.isEmpty()) {
            return ResponseEntity.ok(extractosGenerados);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}