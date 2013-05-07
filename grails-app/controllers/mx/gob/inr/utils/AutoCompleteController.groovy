package mx.gob.inr.utils

import mx.gob.inr.farmacia.SalidaFarmacia;
import grails.converters.JSON

class AutoCompleteController {

	AutoCompleteService autoCompleteService	
	
	def listarPaciente(){
		render autoCompleteService.listarPaciente(params.term) as JSON
	}	
}
