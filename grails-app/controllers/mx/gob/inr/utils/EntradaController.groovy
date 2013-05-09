package mx.gob.inr.utils

import grails.converters.JSON
import mx.gob.inr.farmacia.EntradaFarmaciaService;
import mx.gob.inr.materiales.*;

abstract class EntradaController <E extends Entrada> implements IOperacionController {
		
	public EntradaService entradaService	
	protected entityEntrada
	protected String almacen
	
	public EntradaController(entityEntrada,almacen){
		this.entityEntrada = entityEntrada		
		this.almacen = almacen
	}

	@Override
	def index() {
		redirect(action: "list", params: params)
		
	}

	@Override
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

	@Override
	def list(Integer max) {
		params.max = Math.min(max ?: 10, 100)		
		def entradas = entradaService.listar(params)
		
		[almacen:almacen, entradaInstanceList: entradas.entradaList, entradaInstanceTotal: entradas.entradaTotal]
		
	}

	@Override
	def guardar() {
		E entrada		
		def jsonEntrada = JSON.parse(params.dataPadre)
		def jsonDetalle = JSON.parse(params.dataDetalle)
		def idEntrada = params.int('idPadre')
		
		if(!idEntrada){//Centinela
			entrada = entradaService.setJsonEntrada(jsonEntrada, request.getRemoteAddr())
			entrada = entradaService.guardar(entrada);
			entradaService.guardarDetalle(jsonDetalle[0], entrada, null)
		}
		else{
			entrada = entityEntrada.get(idEntrada)
			entradaService.guardarDetalle(jsonDetalle[0],entrada,null)
		}
		
		render(contentType: 'text/json') {['idPadre': entrada.id]}
		
	}
	
	@Override
	def guardarTodo(){
		
		def jsonEntrada = JSON.parse(params.dataPadre)
		def jsonArrayDetalle = JSON.parse(params.dataArrayDetalle)
		
		def entrada = entradaService.setJsonEntrada(jsonEntrada, request.getRemoteAddr())
		entradaService.guardarTodo(entrada,jsonArrayDetalle)
		
		render(contentType: 'text/json') {['idPadre': entrada.id]}
		
	}

	@Override
	def actualizar() {
		def mensaje = ""
		E entrada
		def jsonEntrada = JSON.parse(params.dataPadre)
		def idEntrada = params.int('idPadre')
		
		entrada = entradaService.setJsonEntrada(jsonEntrada, request.getRemoteAddr())
		
		if(idEntrada){
			mensaje = entradaService.actualizar(entrada, idEntrada)
		}
		
		render(contentType: 'text/json') {['mensaje': mensaje ]}
		
	}

	@Override
	def cancelar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	def consultarDetalle() {
		def json = entradaService.consultarDetalle(params) as JSON
		log.info(json)
		render json
		
	}

	@Override
	def actualizarDetalle(params) {
		
		log.info(params)
		
		def idEntrada = params.long('idPadre')
		def clave = params.long('id')
		def cantidad = params.double('cantidad')
		def precio = params.double('precioEntrada')
		def nolote = params.noLote
		
		Date fechaCaducidad = null
		
		try{
			fechaCaducidad = new Date().parse("dd/MM/yyyy",params.fechaCaducidad)
		}
		catch(Exception e){
			fechaCaducidad = null
		} 
					
		switch(params.oper){
			case "edit":
				entradaService.actualizarDetalle(idEntrada,clave,cantidad,precio,nolote,fechaCaducidad)
				break
			case "del":							
				entradaService.borrarDetalle(idEntrada,clave)
				break
		}
		
		render(contentType: 'text/json') {['responseText': 'weno']}
		
	}

	@Override
	def uniqueFolio() {
		def folioEntrada = params.int('checkFolio')		
		log.info("FolioEntrada: " + folioEntrada)		
		def result = !entradaService.checkFolioEntrada(folioEntrada)		
		render text: result, contentType:"text/html", encoding:"UTF-8"
		
	}

	@Override
	def buscarArticulo() {
		def clave = params.long('id')
		def articulo = entradaService.buscarArticulo(clave)		
		def articuloJSON = articulo as JSON		
		render articuloJSON
		
	}

	@Override
	def listarArticulo() {
		render entradaService.listarArticulo(params.term) as JSON
		
	}

	@Override
	def listarArea() {
		render entradaService.listarArea(params.term) as JSON
		
	}	
	
}
