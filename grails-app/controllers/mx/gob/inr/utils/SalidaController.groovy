package mx.gob.inr.utils

import grails.converters.JSON

abstract class SalidaController <S extends Salida> {

	public EntradaService entradaService
	public SalidaService salidaService	
	
	protected entitySalida
	protected String almacen
	
	public SalidaController(entitySalida, almacen){
		this.entitySalida = entitySalida	
		this.almacen = almacen
	}
	
	
	def disponibilidadArticulo(){
		
		def clave = params.long('clave');
		def fecha = new Date().parse("dd/MM/yyyy", params.fecha)
		
		def disponible = entradaService.disponibilidadArticulo(clave, fecha, almacen)
		
		log.info(String.format("Disponibilidad %s", disponible))
		
		def json = [disponible:disponible] as JSON
		
		log.info(json)
		
		render json
	}
	
	def uniqueFolioSalida(){
		
		def folioSalida  = params.int('checkFolio')
		
		log.info("FolioSalida: " + folioSalida)
		
		def result = !salidaService.checkFolioSalida(folioSalida)
		render text: result, contentType:"text/html", encoding:"UTF-8"
	}
	
	def guardarSalida(){
		
		S salida;
		def jsonSalida = JSON.parse(params.salidaData)
		def jsonDetalle = JSON.parse(params.detalleData)
		def idSalida = params.int('idSalida')
		
		def clave = jsonDetalle[0].cveArt as long
		def solicitado = jsonDetalle[0].solicitado as double
		def surtido = jsonDetalle[0].surtido as double
				
		if(!idSalida){//Centinela
			salida = salidaService.setJsonSalida(jsonSalida, request.getRemoteAddr())
			salida = salidaService.guardar(salida);			
			salidaService.guardarDetalle(clave,solicitado,surtido, salida)
		}
		else{
			salida = entitySalida.get(idSalida)
			salidaService.guardarDetalle(clave,solicitado,surtido, salida)
		}
		
		def disponible = entradaService.disponibilidadArticulo(clave, salida.fechaSalida,almacen)
		
		render(contentType: 'text/json') {['idSalida': salida.id , 'disponible':disponible]}
	}
	
	def actualizarSalida(){
		
		def mensaje = ""
		S salida
		def jsonSalida = JSON.parse(params.salidaData)
		def idSalida = params.int('idSalida')
				
		salida = salidaService.setJsonSalida(jsonSalida, request.getRemoteAddr())	
				
		if(idSalida){
			mensaje = salidaService.actualizar(salida, idSalida)
		}
			
		render(contentType: 'text/json') {['mensaje': mensaje ]}
	}
	
	def cancelarSalida(){
		def idSalida = params.int('idSalida')
		def mensaje = ""
		
		if(idSalida){
			salidaService.cancelar(idSalida);
			mensaje = "Salida Cancelada"
		}
		else
			mensaje = "Error"
			
		render(contentType: 'text/json') {['mensaje': mensaje ]}
	}
	
	def consultarSalidaDetalle(){
		def json = salidaService.consultarDetalle(params) as JSON
		log.info(json)
		render json
	}
	
	def actualizarSalidaDetalle(params){
	
		log.info(params)
		
		def idSalida = params.long('idSalida')
		def clave = params.long('id')
		def solicitado = params.double('solicitado')
		def surtido = params.double('surtido')
					
		switch(params.oper){
			case "edit":
				salidaService.actualizarDetalle(idSalida,clave,solicitado,surtido)
				break
			case "del":
							
				salidaService.borrarDetalle(idSalida,clave)
				break
		}
		
		render(contentType: 'text/json') {['responseText': 'weno']}
	}
	
	public def index() {
		redirect(action: "list", params: params)
	}

	public def list(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		
		def salidas = salidaService.listar(params)
		
		[salidaInstanceList: salidas.salidaList, salidaInstanceTotal: salidas.salidaTotal]
	}

	def create(Integer id) {
		def salidaInstance = entitySalida.newInstance()
		salidaInstance.fechaSalida = new Date()
		salidaInstance.numeroSalida = salidaService.consecutivoNumeroSalida()
		
		log.info("IDDDD " + id)
		
		if(id){
			salidaInstance = entitySalida.get(id)
		}
		
		log.info("Salida www " + salidaInstance.numeroSalida)
		
		[usuariosList:salidaService.usuarios(salidaService.PERFIL_FARMACIA),salidaInstance: salidaInstance]
	}
	
	def listarRecibe(){
		render salidaService.listarRecibe(params.term) as JSON
	}
	
	
	def listarAutoriza(){
		render salidaService.listarAutoriza(params.term) as JSON		
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
