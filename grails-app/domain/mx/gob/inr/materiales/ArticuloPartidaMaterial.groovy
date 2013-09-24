package mx.gob.inr.materiales

import java.io.Serializable;

class ArticuloPartidaMaterial implements Serializable {

    String partida
	Integer cveArt
	String almacen
	Integer movimiento	 	
	
	static mapping = {
		version false
		table 'articulo_partida'
		almacen column:'id_almacen'
		id composite: ['partida','cveArt','almacen','movimiento']
		datasource 'materiales'
	}
}
