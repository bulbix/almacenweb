package mx.gob.inr.utils.domain

import java.util.Date;



abstract class EntradaDetalle implements Serializable {
	
	//Entrada entrada
	Integer renglon
	//Articulo articulo
	Double cantidad
	Double existencia
	Double precioEntrada
	Date fechaCaducidad
	String noLote
	Double restarExistencia
	
	Double importe

}
