package mx.gob.inr.ceye

import grails.converters.JSON
import javax.annotation.PostConstruct;
import mx.gob.inr.farmacia.EntradaFarmaciaService;

import mx.gob.inr.utils.EntradaController;

class EntradaCeyeController extends EntradaController<EntradaCeye> {

	EntradaCeyeService entradaCeyeService	
	
	public EntradaCeyeController(){
		super(EntradaCeye, 'C')
	}
	
	@PostConstruct
	public void init(){
		servicio = entradaCeyeService
	}
	
	def convertidora(){
		def clave = params.long('clave')
		def cantidad = params.int('cantidad')
		def convertidoraJSON = entradaCeyeService.convertidora(clave, cantidad) as  JSON		
		render convertidoraJSON
	}
	
	
	def consultarPaquete(){
		def tipo = params.tipo
		def jsonArray = entradaCeyeService.consultarPaquete(tipo) as JSON
		//log.info(json)
		render jsonArray
	}
  
}
