package com.proyecto.BluesoftBank.entity;

import java.util.List;

import com.proyecto.BluesoftBank.dto.SaldoPorDia;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class Extracto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Cuenta cuenta;

	private int mes;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "extracto_id")
	private List<SaldoPorDia> saldosPorDia;

}