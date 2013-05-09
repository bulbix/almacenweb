package mx.gob.inr.utils

import java.util.Date;

abstract class Entrada {
	
	Integer numeroEntrada
	Date fechaEntrada
	String numeroFactura
	Integer idSalAlma
	Usuario recibio
	Usuario supervisor
	String estadoEntrada = 'A'
	String almacen
	String actividad
	String presupuesto
	Usuario usuario
	Date fechaCaptura = new Date()
	String ipTerminal		
	Integer folioAlmacen
	
	String toString(){
		return numeroEntrada
	}

}
