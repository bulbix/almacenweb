package mx.gob.inr.utils

import grails.converters.JSON
import mx.gob.inr.farmacia.EntradaFarmaciaService;

abstract class EntradaController <E extends Entrada> {

	//static allowedMethods = [guardarEntrada:'POST',jqGridDetalle:'POST']
	
	EntradaService entradaService
	
	protected entityEntrada
	protected entityArea
	protected String almacen
	
	public SalidaController(entityEntrada, entityArea, almacen){
		this.entityEntrada = entityEntrada
		this.entityArea = entityArea
		this.almacen = almacen
	}
	
	

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		
		def listaEntradas = entityEntrada.list(params).each() {
			
			if(it.idSalAlma)
				it.folioAlmacen = SalidaMaterial.get(it.idSalAlma).numeroSalida;
		}
		
		[entradaInstanceList: listaEntradas, entradaInstanceTotal: entityEntrada.count()]
	}

	def create() {
						
		def entradaInstance = entityEntrada.newInstance()
		entradaInstance.fechaEntrada = new Date()
		entradaInstance.numeroEntrada = entradaService.consecutivoNumeroEntrada()
		
		if(id){
			entradaInstance = entityEntrada.get(id)
		}
		
		[usuariosList:entradaService.usuarios(entradaService.PERFIL_FARMACIA),entradaInstance: entradaInstance]
	}
	
	def consultarEntradaDetalle(){
		def json = entradaService.consultarDetalle(params) as JSON
		log.info(json)
		render json
	}
	
	def jqGridMaterial(){
		def folioMaterial = params.int('folioAlmacen')
		def json = entradaService.consultaMaterial(folioMaterial) as JSON
		log.info(json)
		render json
	}
		
	def guardarEntrada(){
		def jsonArrayDetalle = JSON.parse(params.gridEntradaDetalle)
		def jsonArrayEntrada = JSON.parse(params.entradaData)
		entradaService.guardarSalidaMaterial(jsonArrayEntrada, jsonArrayDetalle,request.getRemoteAddr());
		render(contentType: 'text/json') {['status': 'ok']}
	}
		
	def uniqueFolioEntrada(){
		
		def folioEntrada = params.int('checkFolio')		
		log.info("FolioEntrada: " + folioEntrada)		
		def result = !entradaService.checkFolioEntrada(folioEntrada)		
		render text: result, contentType:"text/html", encoding:"UTF-8"
	}
	
	def uniqueFolioSalAlma(){
				
		def result=true
				
		if(params.checkFolioSalAlma){
			def folioSalAlma  = params.int('checkFolio')
			log.info("FolioSalAlma: " + folioSalAlma)
			result = !entradaService.checkFolioSalAlma(folioSalAlma)
		}
		
		render text: result, contentType:"text/html", encoding:"UTF-8"
	}
   
}
