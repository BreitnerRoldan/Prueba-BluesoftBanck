package com.proyecto.BluesoftBank.dto;

public class InsertarSaldoRequest {

	private Long cuentaId;
	private double monto;
	private Long numeroCuenta;
	private String tipocuenta;

	public InsertarSaldoRequest() {
		super();
	}

	public InsertarSaldoRequest(Long cuentaId, double monto, Long numeroCuenta, String tipocuenta) {
		super();
		this.cuentaId = cuentaId;
		this.monto = monto;
		this.numeroCuenta = numeroCuenta;
		this.tipocuenta = tipocuenta;
	}

	public Long getCuentaId() {
		return cuentaId;
	}

	public void setCuentaId(Long cuentaId) {
		this.cuentaId = cuentaId;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public Long getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(Long numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getTipocuenta() {
		return tipocuenta;
	}

	public void setTipocuenta(String tipocuenta) {
		this.tipocuenta = tipocuenta;
	}

	
}