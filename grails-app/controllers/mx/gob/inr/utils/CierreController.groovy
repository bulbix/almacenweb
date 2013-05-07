package mx.gob.inr.utils

import mx.gob.inr.farmacia.CierreFarmaciaService;

abstract class CierreController <C extends Cierre>  {

    CierreService cierreService
	
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
        params.max = Math.min(max ?: 10, 100)
        [cierreInstanceList: entityCierre.list(params), cierreInstanceTotal: entityCierre.count()]
    }

    def create() {
		
		def cierreInstance = entityCierre.newInstance()		
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
}
