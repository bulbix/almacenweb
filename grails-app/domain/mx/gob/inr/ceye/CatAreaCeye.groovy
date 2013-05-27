package mx.gob.inr.ceye

import mx.gob.inr.utils.domain.CatArea;

class CatAreaCeye extends CatArea {

	static mapping = {
		id column:'cve_area'
		version false
		table 'cat_area_ceye'
	}
	
    static constraints = {
    }
}
