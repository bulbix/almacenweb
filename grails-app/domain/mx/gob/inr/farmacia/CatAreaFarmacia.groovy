package mx.gob.inr.farmacia

import mx.gob.inr.utils.CatArea

class CatAreaFarmacia extends CatArea {
	    
	static constraints = {
    }
	
	static mapping = {
		id column:'cve_area'
		version false
		table 'cat_area'
	}
}
