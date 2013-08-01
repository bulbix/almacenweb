package mx.gob.inr.utils

import mx.gob.inr.farmacia.CierreFarmaciaService;
import mx.gob.inr.utils.domain.Cierre;
import mx.gob.inr.utils.services.CierreService;

abstract class CierreController <C extends Cierre>  {

    public CierreService cierreService	
	protected entityCierre	
	
	
	public CierreController(entityCierre){
		this.entityCierre = entityCierre
	}   

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
		
		if(params.almacen){
			session.almacen = params.almacen
		}	
		
		def result = cierreService.listar(session.almacen)		
		[almacen:session.almacen, cierreInstanceList: result.lista, cierreInstanceTotal: result.total]
		
    }

    def create() {
		
		def cierreInstance = entityCierre.newInstance()
		cierreInstance.fechaCierre = new Date()		
        [almacen:session.almacen,cierreInstance: cierreInstance]
    }
	
	def generarCierrre(){
		Date fechaCierre = new Date().parse("dd/MM/yyyy", params.fechaCierre)
		cierreService.cerrarPeriodo(fechaCierre,session.almacen);
		
		log.info("Value Progress " + cierreService.valueProgress)
		render(contentType: 'text/json') {['status': 'ok']}
	}
	
	def consultarValue(){
		
		def value = cierreService?.valueProgress		
		log.info("VALOR PORCENTAJE " + value)		
		render(contentType: 'text/json') {['value': value ]}
	}
	
	def checkCierre(){
		Date fecha = null
		try{
			fecha =  new Date().parse("dd/MM/yyyy",params.value)
		}
		catch(Exception e){
			fecha = null
		}
		
		def result = !cierreService.checkCierre(fecha,session.almacen)
		render text: result, contentType:"text/html", encoding:"UTF-8"
	}
	
	def eliminar(){		
		Date fechaCierre = new Date().parse("dd/MM/yyyy", params.fechaCierre)		
		def result = cierreService.eliminar(fechaCierre,session.almacen)
		
		if(!result)		
			flash.message = "Hay cierres posteriores"		
		
		redirect(action: "list", params: params)
	}
	
	def reporte(){
		params.SUBREPORT_DIR = "${servletContext.getRealPath('/reports')}/"
		params.IMAGE_DIR = "${servletContext.getRealPath('/images')}/"
		params.locale =  new Locale("es","MX");
		
		Date fechaCierre = new Date().parse("dd/MM/yyyy", params.fechaCierre)
		def data = cierreService.reporte(fechaCierre,session.almacen)
		chain(controller: "jasper", action: "index", model: [data:data], params:params)		
	}
		
}
