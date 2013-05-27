package mx.gob.inr.farmacia

import mx.gob.inr.utils.domain.Cierre;

class CierreFarmacia extends Cierre {

	ArticuloFarmacia articulo
	
    static constraints = {
    }
	
	
	static mapping = {		
		version false
		table 'cierre'
		articulo column:'cve_art'
		id composite: ['fechaCierre','articulo']
	}
	
}
