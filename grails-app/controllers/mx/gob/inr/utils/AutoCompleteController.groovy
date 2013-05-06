package mx.gob.inr.utils

import mx.gob.inr.farmacia.SalidaFarmacia;
import grails.converters.JSON

class AutoCompleteController {

	AutoCompleteService autoCompleteService	
    
	def artlist(){
		render autoCompleteService.artlist(params) as JSON		
	}
	
	def arealist(){
		render autoCompleteService.arealist(params) as JSON
	}
	
	def pacientelist(){
		render autoCompleteService.pacientelist(params) as JSON
	}
	
	
	def recibelist(){
		render autoCompleteService.stringlist(SalidaFarmacia, "recibio",params.term) as JSON
	}
	
	
	def autorizalist(){
		render autoCompleteService.stringlist(SalidaFarmacia, "jefeServicio",params.term) as JSON
	}
	
}
