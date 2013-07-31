package mx.gob.inr.utils.domain

abstract class Articulo {
	
	String desArticulo
	String unidad
	String presentacion
	Double movimientoProm	
	String almacen
	
	String toString(){
		return String.format("(%s) %s", id, desArticulo)
	}

}
