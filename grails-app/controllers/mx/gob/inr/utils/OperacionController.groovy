package mx.gob.inr.utils

import grails.converters.JSON
import mx.gob.inr.materiales.*

abstract class OperacionController<A> implements IOperacionController {

	public IOperacionService<A> servicio
	protected entityAlmacen
	protected String almacen
	
	
	public OperacionController(entityAlmacen, almacen){
		this.entityAlmacen = entityAlmacen
		this.almacen = almacen
	}
	
	@Override
    def index(){
		redirect(action: "list", params: params)	
	}
	
	
	@Override
	def create(Integer id){
		def almacenInstance = entityAlmacen.newInstance()
		almacenInstance.fecha = new Date()
		almacenInstance.folio = servicio.consecutivoFolio()
		almacenInstance.almacen = almacen
		
		boolean existeCierre = false	
		
		if(id){
			almacenInstance = entityAlmacen.get(id)
			
			existeCierre = servicio.checkCierre(almacenInstance.fecha)
			
			if(almacenInstance instanceof Entrada){
				if(almacenInstance.idSalAlma)
					almacenInstance.folioAlmacen =SalidaMaterial.get(almacenInstance.idSalAlma).folio
			}
			
		}
		
		def usuariosList = null
		
		if(almacen == 'F'){
			usuariosList = servicio.usuarios(servicio.PERFIL_FARMACIA)
		}
		else{
			usuariosList = servicio.usuarios(servicio.PERFIL_CEYE)
		}		
		
		[usuariosList:usuariosList,almacenInstance: almacenInstance, existeCierre:existeCierre]
	}
	
	@Override
	def list(Integer max){
		
		params.max = Math.min(max ?: 10, 100)
		def result = servicio.listar(params)
		
		[almacen:almacen, almacenInstanceList: result.lista, almacenInstanceTotal: result.total]
		
		
	}
	
	@Override
	def guardar(){
		
		A almacen
		def jsonPadre = JSON.parse(params.dataPadre)
		def jsonDetalle = JSON.parse(params.dataDetalle)
		def idPadre = params.int('idPadre')
		
		if(!idPadre){//Centinela
			almacen = servicio.setJson(jsonPadre, request.getRemoteAddr())
			almacen =  servicio.guardar(almacen);
			servicio.guardarDetalle(jsonDetalle[0], almacen, null)
		}
		else{
			almacen = entityAlmacen.get(idPadre)
			servicio.guardarDetalle(jsonDetalle[0],almacen,null)
		}		
		render(contentType: 'text/json') {['idPadre': almacen.id]}
		
	}
	
	@Override
	def guardarTodo(){
		def jsonPadre = JSON.parse(params.dataPadre)
		def jsonArrayDetalle = JSON.parse(params.dataArrayDetalle)
		
		def almacen = servicio.setJson(jsonPadre, request.getRemoteAddr())
		servicio.guardarTodo(almacen,jsonArrayDetalle)
		
		render(contentType: 'text/json') {['idPadre': almacen.id]}
		
	}
	
	@Override
	def actualizar(){
		
		def mensaje = ""
		A almacen
		def jsonPadre = JSON.parse(params.dataPadre)
		def idPadre = params.int('idPadre')
		
		almacen = servicio.setJson(jsonPadre, request.getRemoteAddr())
		
		if(idPadre){
			mensaje = servicio.actualizar(almacen, idPadre)
		}
		
		render(contentType: 'text/json') {['mensaje': mensaje ]}
		
	}
	
	@Override
	def cancelar(){
		
		def idPadre = params.int('idPadre')
		def mensaje = ""
		
		if(idPadre){
			servicio.cancelar(idPadre);
			mensaje = "Cancelado"
		}
		else
			mensaje = "Error"
			
		render(contentType: 'text/json') {['mensaje': mensaje ]}
		
	}
	
	@Override
	def consultarDetalle(){
		def json = servicio.consultarDetalle(params) as JSON
		log.info(json)
		render json
		
	}
	
	@Override
	def actualizarDetalle(params){
		
		def idPadre = params.long('idPadre')
		def clave = params.long('id')
		def mensaje = ""
		
		switch(params.oper){
			case "edit":
				mensaje = servicio.actualizarDetalle(idPadre,params)
				break
			case "del":
				mensaje = servicio.borrarDetalle(idPadre,clave)
				break
		}
		
		render(contentType: 'text/json') {['mensaje': mensaje]}
		
	}
	
	@Override
	def uniqueFolio(){
		def folio = params.int('value')		
		def result = !servicio.checkFolio(folio)
		render text: result, contentType:"text/html", encoding:"UTF-8"
	}
	
	@Override
	def buscarArticulo(){
		def clave = params.long('id')
		def articulo = servicio.buscarArticulo(clave)
		def articuloJSON = articulo as JSON
		render articuloJSON
		
	}
	
	@Override
	def checkCierre(){
		Date fecha = null		
		try{
			fecha =  new Date().parse("dd/MM/yyyy",params.value)
		}
		catch(Exception e){
			fecha = null
		}
		
		def result = !servicio.checkCierre(fecha)
		render text: result, contentType:"text/html", encoding:"UTF-8"
	}
	
	@Override
	def listarArticulo(){
		render servicio.listarArticulo(params.term) as JSON
	}
	
	@Override
	def listarArea(){
		render servicio.listarArea(params.term) as JSON
	}
}
