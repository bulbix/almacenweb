package mx.gob.inr.utils

import java.util.Date;

import mx.gob.inr.farmacia.CatAreaFarmacia;

abstract class Salida {

	Integer numeroSalida
	Date fechaSalida
	String jefeServicio
	String recibio
	Usuario entrego
	String estadoSalida = 'A'
	String almacen
	CatArea area
	String noOrden
	Usuario usuario
	String ipTerminal
	Date fechaCaptura = new Date()
	Paciente paciente
}
