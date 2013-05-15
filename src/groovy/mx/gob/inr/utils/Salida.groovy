package mx.gob.inr.utils

import java.util.Date;

import mx.gob.inr.farmacia.CatAreaFarmacia;

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
	Paciente paciente
}
