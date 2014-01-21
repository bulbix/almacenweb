package mx.gob.inr.ceye

import mx.gob.inr.utils.domain.CatArea;

class CatAreaCeye extends CatArea {
	
	String almacen

	static mapping = {
		id column:'cve_area'
		version false
		table 'cat_area_ceye'
		cache usage:'read-only'
	}
	
    static constraints = {
    }
}
