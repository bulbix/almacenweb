package mx.gob.inr.utils.domain

abstract class Cierre implements Serializable {

	Date fechaCierre
	String almacen
	//Articulo articulo
	Integer existencia
	Double importe
}
