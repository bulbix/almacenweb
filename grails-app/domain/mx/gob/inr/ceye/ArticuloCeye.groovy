package mx.gob.inr.ceye

import mx.gob.inr.farmacia.ArticuloFarmacia
import mx.gob.inr.utils.Partida;
import mx.gob.inr.utils.domain.Articulo;

class ArticuloCeye extends Articulo {

	 Partida partida
	 
	 //Propiedades transient para la convertidora
	 String descripcionAlmacen
	 String unidadAlmacen
	 Float cantidadAlmacen
	 Float cantidadCeye
	 
	 static transients = ['descripcionAlmacen','unidadAlmacen','cantidadAlmacen','cantidadCeye']
	 
     static mapping = {	 
		
		id generator:'assigned',column:'cve_art'		
		version false		
		table 'articulo_ceye'
		partida column:'partida'		
		sort "id"
		cache true
	}
}
