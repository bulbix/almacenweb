package mx.gob.inr.ceye

import grails.converters.JSON
import javax.annotation.PostConstruct;
import mx.gob.inr.farmacia.EntradaFarmaciaService;
import mx.gob.inr.utils.EntradaController;
import grails.plugins.springsecurity.Secured;

@Secured(['ROLE_CEYE'])
class EntradaCeyeController extends EntradaController<EntradaCeye> {

	EntradaCeyeService entradaCeyeService	
	
	public EntradaCeyeController(){
		super(EntradaCeye)
	}
	
	@PostConstruct
	public void init(){		
		servicio = entradaCeyeService
	}
	
	def convertidora(){
		def clave = params.long('clave')
		def cantidad = params.int('cantidad')
		def convertidoraJSON = entradaCeyeService.convertidora(clave, cantidad) as  JSON
		log.info(convertidoraJSON)		
		render convertidoraJSON
	}
	
	
	def consultarPaquete(){
		def tipo = params.tipo
		def jsonArray = entradaCeyeService.consultarPaquete(tipo) as JSON
		//log.info(json)
		render jsonArray
	}
	
	def listarRecibe(){
		render entradaCeyeService.listarRecibe(params.term) as JSON
	}
		
	def listarSolicita(){
		render entradaCeyeService.listarSolicita(params.term) as JSON
	}
  
}
