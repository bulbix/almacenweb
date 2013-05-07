package mx.gob.inr.utils

import grails.converters.JSON
import mx.gob.inr.farmacia.EntradaFarmaciaService;

abstract class EntradaController <E extends Entrada> {

	//static allowedMethods = [guardarEntrada:'POST',jqGridDetalle:'POST']
	
	public EntradaService entradaService
	
	protected entityEntrada
	protected String almacen
	
	public EntradaController(entityEntrada,almacen){
		this.entityEntrada = entityEntrada		
		this.almacen = almacen
	}
	

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.max = Math.min(max ?: 10, 100)		
		def entradas = entradaService.listar(params)
		
		[entradaInstanceList: entradas.entradaList, entradaInstanceTotal: entradas.entradaTotal]
	}

	def create(Integer id) {
						
		def entradaInstance = entityEntrada.newInstance()
		entradaInstance.fechaEntrada = new Date()
		entradaInstance.numeroEntrada = entradaService.consecutivoNumeroEntrada()
		entradaInstance.almacen = almacen
		
		if(id){
			entradaInstance = entityEntrada.get(id)
			
			if(entradaInstance.idSalAlma)
				entradaInstance.folioAlmacen = SalidaMaterial.get(entradaInstance.idSalAlma).numeroSalida
			
		}
		
		def usuariosList = null
		
		if(almacen == 'F'){
			usuariosList = entradaService.usuarios(entradaService.PERFIL_FARMACIA)
		}
		else{
			usuariosList = entradaService.usuarios(entradaService.PERFIL_CEYE)
		}		
		
		[usuariosList:usuariosList,entradaInstance: entradaInstance]
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
		
		E entrada		
		def jsonEntrada = JSON.parse(params.entradaData)
		def jsonDetalle = JSON.parse(params.detalleData)
		def idEntrada = params.int('idEntrada')
		
		if(!idEntrada){//Centinela
			entrada = entradaService.setJsonEntrada(jsonEntrada, request.getRemoteAddr())
			entrada = entradaService.guardar(entrada);
			entradaService.guardarDetalle(jsonDetalle, entrada)
		}
		else{
			entrada = entityEntrada.get(idEntrada)
			entradaService.guardarDetalle(jsonDetalle,entrada)
		}
		
		render(contentType: 'text/json') {['idEntrada': entrada.id]}
		
	}
	
	def guardarSalidaMaterial(){
		
		def jsonEntrada = JSON.parse(params.entradaData)
		def jsonDetalle = JSON.parse(params.gridEntradaDetalle)
		
		def entrada = entradaService.setJsonEntrada(jsonEntrada, request.getRemoteAddr())
		entradaService.guardarSalidaMaterial(entrada, jsonDetalle);
		
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
	
	
	def buscarArticulo(){
		def clave = params.long('id')
		def articulo = entradaService.buscarArticulo(clave)		
		def articuloJSON = articulo as JSON		
		render articuloJSON
	}
	
	def listarArticulo(){
		render entradaService.listarArticulo(params.term) as JSON
	}
	
	
	def listarArea(){
		render entradaService.listarArea(params.term) as JSON
	}
   
}
