package mx.gob.inr.ceye

import mx.gob.inr.farmacia.ArticuloFarmacia
import mx.gob.inr.utils.Articulo
import mx.gob.inr.utils.Partida;

class ArticuloCeye extends Articulo {

	 Partida partida
	
     static mapping = {
		id column:'cve_art'
		version false		
		table 'articulo_ceye'
		partida column:'partida'
	}
}
