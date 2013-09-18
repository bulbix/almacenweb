package mx.gob.inr.utils.domain

import java.util.Date;

import mx.gob.inr.seguridad.Usuario;

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
	Date fechaModificacion = new Date()
	String ipTerminal		
	Integer folioAlmacen
	
	boolean dueno
		
	
	String toString(){
		return folio
	}
	
	

}
