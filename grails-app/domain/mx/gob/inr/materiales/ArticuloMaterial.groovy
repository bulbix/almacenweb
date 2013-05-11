package mx.gob.inr.materiales

import mx.gob.inr.utils.Articulo

class ArticuloMaterial extends Articulo {

    static mapping = {
		id column:'cve_art'
		version false		
		table 'articulo'
		datasource 'materiales'
	}
}
