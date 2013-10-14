package mx.gob.inr.utils

import mx.gob.inr.farmacia.SalidaFarmacia;
import grails.converters.JSON
import grails.plugin.jsonp.JSONP;

class AutoCompleteController {

	AutoCompleteService autoCompleteService	
	
	def listarPaciente(){
				
		def pacientes = autoCompleteService.listarPaciente(params.term)
		
		if (params.callback) {
			render "${params.callback}(${pacientes as JSON})"
		} else {
			render pacientes as JSON
		}
		
	}	
}
