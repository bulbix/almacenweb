package mx.gob.inr.utils.domain

import java.util.Date;

import mx.gob.inr.farmacia.CatAreaFarmacia;
import mx.gob.inr.utils.Paciente;
import mx.gob.inr.seguridad.Usuario;

abstract class Salida {

	Integer folio
	Date fecha
	String jefeServicio
	String recibio
	Usuario entrego
	String estado = 'A'
	String almacen
	//CatArea area
	String noOrden
	Usuario usuario
	String ipTerminal
	Date fechaCaptura = new Date()
	Date fechaModificacion = new Date()
	Paciente paciente
	
	boolean dueno
}
