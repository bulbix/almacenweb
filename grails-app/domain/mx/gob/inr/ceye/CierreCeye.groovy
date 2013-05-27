package mx.gob.inr.ceye

import mx.gob.inr.utils.domain.Cierre;

class CierreCeye extends Cierre {

	ArticuloCeye articulo
	
	static mapping = {
		version false
		table 'cierre_ceye'
		articulo column:'cve_art'
		id composite: ['fechaCierre','articulo']
	}
	
	
    static constraints = {
    }
}
