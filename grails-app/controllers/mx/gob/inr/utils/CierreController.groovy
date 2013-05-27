package mx.gob.inr.utils

import mx.gob.inr.farmacia.CierreFarmaciaService;
import mx.gob.inr.utils.domain.Cierre;
import mx.gob.inr.utils.services.CierreService;

abstract class CierreController <C extends Cierre>  {

    public CierreService cierreService
	
	protected entityCierre	
	protected String almacen
	
	public CierreController(entityCierre, almacen){
		this.entityCierre = entityCierre		
		this.almacen = almacen
	}   

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
		
		def result = cierreService.listar()		
		[almacen:almacen, cierreInstanceList: result.lista, cierreInstanceTotal: result.total]
		
    }

    def create() {
		
		def cierreInstance = entityCierre.newInstance()
		cierreInstance.fechaCierre = new Date()		
        [cierreInstance: cierreInstance]
    }
	
	def generarCierrre(){
		Date fechaCierre = new Date().parse("dd/MM/yyyy", params.fechaCierre)
		cierreService.cerrarPeriodo(fechaCierre);
		
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
		
		def result = !cierreService.checkCierre(fecha)
		render text: result, contentType:"text/html", encoding:"UTF-8"
	}
	
	def eliminar(){		
		Date fechaCierre = new Date().parse("dd/MM/yyyy", params.fechaCierre)		
		def result = cierreService.eliminar(fechaCierre)
		
		if(!result)		
			flash.message = "Hay cierres posteriores"		
		
		redirect(action: "list", params: params)
	}
	
	def reporte(){
		
		params.IMAGE_DIR = "${servletContext.getRealPath('/images')}/"
		Date fechaCierre = new Date().parse("dd/MM/yyyy", params.fechaCierre)
		def data = cierreService.reporte(fechaCierre)
		chain(controller: "jasper", action: "index", model: [data:data], params:params)		
	}
		
}
