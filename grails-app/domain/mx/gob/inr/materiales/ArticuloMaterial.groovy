package mx.gob.inr.materiales

import mx.gob.inr.utils.domain.Articulo;

class ArticuloMaterial extends Articulo implements Serializable {

	//String partida
	Integer cveArt
	
    static mapping = {
		version false		
		table 'articulo'
		almacen column:'id_almacen'
		id composite: ['cveArt','almacen']
		datasource 'materiales'
	}
}
