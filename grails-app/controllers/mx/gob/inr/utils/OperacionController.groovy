package mx.gob.inr.utils

import grails.converters.JSON
import mx.gob.inr.farmacia.EntradaDetalleFarmacia;
import mx.gob.inr.materiales.*
import mx.gob.inr.utils.domain.Entrada;
import mx.gob.inr.utils.services.IOperacionService
import mx.gob.inr.farmacia.EntradaFarmacia;
import mx.gob.inr.util.services.*;
import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService;

abstract class OperacionController<A> implements IOperacionController {

	public IOperacionService<A> servicio
	protected entityAlmacen
		
	def filterPaneService
	SpringSecurityService springSecurityService
	
	public OperacionController(entityAlmacen){
		this.entityAlmacen = entityAlmacen		
	}
	
	@Override	
    def index(){
		redirect(action: "list", params: params)	
	}
	
	
	@Override
	def create(Integer id){
		
		if(params.almacen){
			session.almacen = params.almacen
		}	
		
		
		def almacenInstance = entityAlmacen.newInstance()
		almacenInstance.fecha = new Date()
		almacenInstance.folio = servicio.consecutivoFolio(session.almacen)
		almacenInstance.almacen = session.almacen
		
		boolean existeCierre = false	
		
		if(id){
			almacenInstance = entityAlmacen.get(id)
			
			existeCierre = servicio.checkCierre(almacenInstance.fecha,session.almacen)
			
			if(almacenInstance instanceof Entrada){
				if(almacenInstance.idSalAlma)
					almacenInstance.folioAlmacen =SalidaMaterial.get(almacenInstance.idSalAlma)?.folio
			}
			
		}
		
		def usuariosList = null
		
		if(session.almacen == 'F'){
			usuariosList = servicio.usuarios(servicio.PERFIL_FARMACIA)
		}
		else{
			usuariosList = servicio.usuarios(servicio.PERFIL_CEYE)
		}		
		
		def isDueno = almacenInstance.usuario == springSecurityService.currentUser
		
		[usuariosList:usuariosList,almacenInstance: almacenInstance,existeCierre:existeCierre, isDueno:isDueno]
	}
	
	@Override
	def eliminar(Integer id ){
		def almacenInstance = entityAlmacen.get(id)
		almacenInstance.delete()
		redirect(action: "list", params: params)
	}
	
	@Override	
	def list(Integer max){	
		
		if(params.almacen){
			session.almacen = params.almacen
		}	
		
		params.almacen = session.almacen
		params.max = Math.min(max ?: 10, 100)
		def result = servicio.listar(params,springSecurityService.currentUser)		
		
		[almacen:session.almacen, almacenInstanceList: result.lista, almacenInstanceTotal: result.total]
		
		
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
		
		render( view:'list',model:[almacen:session.almacen, almacenInstanceList: almacenList,
			almacenInstanceTotal: filterPaneService.count( params, entityAlmacen ),
			filterParams: org.grails.plugin.filterpane.FilterPaneUtils.extractFilterParams(params),
			params:params ] )
	}
	
	@Override
	def guardar(){
		
		A entity
		def jsonPadre = JSON.parse(params.dataPadre)
		def jsonDetalle = JSON.parse(params.dataDetalle)
		def idPadre = params.int('idPadre')
		
		if(!idPadre){//Centinela
			entity = servicio.setJson(jsonPadre, request.getRemoteAddr(), springSecurityService.currentUser,session.almacen)
			entity =  servicio.guardar(entity);
			servicio.guardarDetalle(jsonDetalle[0], entity, null,session.almacen)
		}
		else{
			entity = entityAlmacen.get(idPadre)
			servicio.guardarDetalle(jsonDetalle[0],entity,null,session.almacen)
		}		
		render(contentType: 'text/json') {['idPadre': entity.id]}
		
	}
	
	@Override
	def guardarTodo(){
		def jsonPadre = JSON.parse(params.dataPadre)
		def jsonArrayDetalle = JSON.parse(params.dataArrayDetalle)
		
		def almacen = servicio.setJson(jsonPadre, request.getRemoteAddr(), springSecurityService.currentUser,session.almacen)
		def mensaje = servicio.guardarTodo(almacen,jsonArrayDetalle,session.almacen)
		
		render(contentType: 'text/json') {['idPadre': almacen.id,'mensaje':mensaje]}
		
	}
	
	@Override
	def actualizar(){
		
		def mensaje = ""
		A almacen
		def jsonPadre = JSON.parse(params.dataPadre)
		def idPadre = params.int('idPadre')
		
		almacen = servicio.setJson(jsonPadre, request.getRemoteAddr(), springSecurityService.currentUser,session.almacen)
		
		if(idPadre){
			try{
				mensaje = servicio.actualizar(almacen, idPadre,session.almacen)
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
		
		log.info(idPadre)
		
		if(idPadre){
			mensaje = servicio.cancelar(idPadre, session.almacen);			
		}
		else
			mensaje = "Error"
		
		log.info(mensaje)	
		flash.message = mensaje
			
		render(contentType: 'text/json') {['mensaje': mensaje ]}
		
	}
	
	@Override
	def consultarDetalle(){
		params.almacen = session.almacen
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
				mensaje = servicio.actualizarDetalle(idPadre,params,session.almacen)
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
		def result = !servicio.checkFolio(folio,session.almacen)
		render text: result, contentType:"text/html", encoding:"UTF-8"
	}
	
	@Override
	def buscarArticulo(){
		def clave = params.long('id')
		def articulo = servicio.buscarArticulo(clave,session.almacen)
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
		
		def result = !servicio.checkCierre(fecha,session.almacen)
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
		
		params.SUBREPORT_DIR = "${servletContext.getRealPath('/reports')}/"
		params.IMAGE_DIR = "${servletContext.getRealPath('/images')}/"
		
		long id=params.long('id')		
		def data = servicio.reporte(id,session.almacen)
		chain(controller: "jasper", action: "index", model: [data:data], params:params)
	}
	
}
