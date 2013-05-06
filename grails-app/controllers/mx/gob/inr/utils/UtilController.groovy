package mx.gob.inr.utils

import mx.gob.inr.farmacia.ArticuloFarmacia;
import grails.converters.JSON

class UtilController {
    
	def buscarArticulo(){
		def articulo =  ArticuloFarmacia.get(params.id)
		articulo.desArticulo = articulo.desArticulo.trim()
		def articuloJSON = articulo as JSON
		log.info(articuloJSON) 		
		render articuloJSON
	}
}
