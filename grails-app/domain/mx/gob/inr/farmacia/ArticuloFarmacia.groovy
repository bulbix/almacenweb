package mx.gob.inr.farmacia

import mx.gob.inr.utils.Articulo

class ArticuloFarmacia extends Articulo {

	static mapping = {
		id column:'cve_art'
		version false		
		table 'articulo'
		datasources (['DEFAULT', 'materiales'])
	}
	
}