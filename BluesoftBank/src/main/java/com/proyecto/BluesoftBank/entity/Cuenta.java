package com.proyecto.BluesoftBank.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@JsonIgnoreProperties("movimientos")
public class Cuenta {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String numeroCuenta;
    private String tipoCuenta;
    private double saldo;

    @ManyToOne
    private Cliente cliente;

    @OneToMany(mappedBy = "cuenta")
    private List<Movimiento> movimientos;
    
}
