package com.proyecto.BluesoftBank.service;


import com.proyecto.BluesoftBank.Exception.CuentaNotFoundException;
import com.proyecto.BluesoftBank.Exception.SaldoNegativoException;
import com.proyecto.BluesoftBank.entity.Cuenta;

public interface CuentaService {

	 Double consultarSaldo(Long cuentaId)throws CuentaNotFoundException;
	 
	 void insertarSaldo(Long cuentaId, double monto) throws CuentaNotFoundException, SaldoNegativoException;
	 
	 void save(Cuenta cuenta);
	 
	 Cuenta guardarCuenta(Cuenta cuenta)throws CuentaNotFoundException;
	 
	 

}
