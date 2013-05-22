package mx.gob.inr.utils

import grails.converters.JSON
import mx.gob.inr.farmacia.EntradaDetalleFarmacia;
import mx.gob.inr.materiales.*

import mx.gob.inr.farmacia.EntradaFarmacia;

abstract class OperacionController<A> implements IOperacionController {

	public IOperacionService<A> servicio
	protected entityAlmacen
	protected String almacen
	
	
	def filterPaneService
	
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
	def eliminar(Integer id ){
		def almacenInstance = entityAlmacen.get(id)
		almacenInstance.delete()
		redirect(action: "list", params: params)
	}
	
	@Override
	def list(Integer max){
		
		params.max = Math.min(max ?: 10, 100)
		def result = servicio.listar(params)
		
		[almacen:almacen, almacenInstanceList: result.lista, almacenInstanceTotal: result.total]
		
		
	}
	
	def filter = {
		if(!params.max) params.max = 10
		
		def almacenList = filterPaneService.filter( params, entityAlmacen)
		
		if(entityAlmacen.name.contains("EntradaFarmacia")){
			almacenList.each(){
				if(it.idSalAlma)
					it.folioAlmacen = SalidaMaterial.get(it.idSalAlma).folio;
			}
		}	
		
		render( view:'list',model:[almacen:almacen, almacenInstanceList: almacenList,
			almacenInstanceTotal: filterPaneService.count( params, entityAlmacen ),
			filterParams: org.grails.plugin.filterpane.FilterPaneUtils.extractFilterParams(params),
			params:params ] )
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
		def mensaje = servicio.guardarTodo(almacen,jsonArrayDetalle)
		
		render(contentType: 'text/json') {['idPadre': almacen.id,'mensaje':mensaje]}
		
	}
	
	@Override
	def actualizar(){
		
		def mensaje = ""
		A almacen
		def jsonPadre = JSON.parse(params.dataPadre)
		def idPadre = params.int('idPadre')
		
		almacen = servicio.setJson(jsonPadre, request.getRemoteAddr())
		
		if(idPadre){
			try{
				mensaje = servicio.actualizar(almacen, idPadre)
			}
			catch(AlmacenException e){
				mensaje = e.message
			}
		}
		
		render(contentType: 'text/json') {['mensaje': mensaje ]}
		
	}
	
	@Override
	def cancelar(){
		
		def idPadre = params.int('idPadre')
		def mensaje = ""
		
		if(idPadre){
			mensaje = servicio.cancelar(idPadre);			
		}
		else
			mensaje = "Error"
			
		flash.message = mensaje
			
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
	
	@Override
	def reporte() {
		long id=params.long('id')
		def data = servicio.reporte(id)
		chain(controller: "jasper", action: "index", model: [data:data], params:params)
	}
	
}
