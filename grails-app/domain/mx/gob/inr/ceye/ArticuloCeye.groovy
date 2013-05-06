package mx.gob.inr.ceye

import mx.gob.inr.farmacia.ArticuloFarmacia
import mx.gob.inr.utils.Articulo

class ArticuloCeye extends Articulo {

     static mapping = {
		id column:'cve_art'
		version false		
		table 'articulo_ceye'
	}
}
