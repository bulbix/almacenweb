package mx.gob.inr.utils

import java.util.Date;

abstract class Entrada {
	
	Integer folio
	Date fecha
	String numeroFactura
	Integer idSalAlma
	Usuario recibio
	Usuario supervisor
	String estado = 'A'
	String almacen
	String actividad
	String presupuesto
	Usuario usuario
	Date fechaCaptura = new Date()
	String ipTerminal		
	Integer folioAlmacen
	
	String toString(){
		return folio
	}

}
