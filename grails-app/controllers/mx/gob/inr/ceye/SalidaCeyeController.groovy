package mx.gob.inr.ceye

import mx.gob.inr.farmacia.SalidaFarmacia;
import mx.gob.inr.utils.SalidaController;
import grails.converters.JSON
import javax.annotation.PostConstruct;
import mx.gob.inr.farmacia.EntradaFarmaciaService;
import mx.gob.inr.farmacia.SalidaFarmaciaService;
import grails.plugins.springsecurity.Secured;

@Secured(['ROLE_CEYE'])
class SalidaCeyeController extends SalidaController<SalidaCeye> {
	SalidaCeyeService salidaCeyeService
	
	public SalidaCeyeController(){
		super(SalidaCeye)
	}
	
	@PostConstruct
	public void init(){		
		servicio = salidaCeyeService
		super.salidaService = salidaCeyeService
	}
	
	def consultarPaquete(){
		def tipo = params.tipo
		def fecha = new Date().parse("dd/MM/yyyy",params.fecha) 
		def jsonArray = salidaCeyeService.consultarPaquete(tipo,fecha,session.almacen) as JSON
		render jsonArray
	}
}
