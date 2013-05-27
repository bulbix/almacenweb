package mx.gob.inr.utils

import javax.annotation.PostConstruct;

import mx.gob.inr.utils.domain.Salida;
import mx.gob.inr.utils.services.SalidaService;

import grails.converters.JSON

class SalidaController <S extends Salida> extends OperacionController<S> {

	public SalidaService salidaService	
	
	public SalidaController(entitySalida, almacen){
		super(entitySalida,almacen)		
	}
	
	////////////METODOS PROPIOS////////////////////////////////
	
	def disponibilidadArticulo(){
		
		def clave = params.long('clave');
		def fecha = new Date().parse("dd/MM/yyyy", params.fecha)
		
		def disponible = salidaService.disponibilidadArticulo(clave, fecha)
		
		log.info(String.format("Disponibilidad %s", disponible))
		
		def json = [disponible:disponible] as JSON
		
		log.info(json)
		
		render json
	}
	
	def listarProcedimiento(){
		render salidaService.listarProcedimiento(params.term) as JSON
	}
	
	def listarRecibe(){
		render salidaService.listarRecibe(params.term) as JSON
	}
		
	def listarAutoriza(){
		render salidaService.listarAutoriza(params.term) as JSON
	}
	
	
	
	
	
}
