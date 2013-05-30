package mx.gob.inr.farmacia

import mx.gob.inr.utils.domain.Inventario

class InventarioFarmacia extends Inventario {

    ArticuloFarmacia articulo
	
	static mapping = {
		version false
		table 'conteos'
		articulo column:'cve_art'
		id composite: ['fechInv','articulo']
	}
	
	static constraints = {
	}
}
