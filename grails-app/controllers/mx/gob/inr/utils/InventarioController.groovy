package mx.gob.inr.utils

import mx.gob.inr.utils.services.CierreService;
import mx.gob.inr.utils.services.InventarioService

class InventarioController {

	public InventarioService inventarioService
	
	
	protected entityCierre	
	protected String almacen
	
	public InventarioController(entityCierre, almacen){
		this.entityCierre = entityCierre		
		this.almacen = almacen
	}   
	
	def create(){		
		def fechaCierre = inventarioService.utilService.maximaFechaCierre(entityCierre, almacen)		
		[fechaCierre:fechaCierre]		
	}
	
	
	def asignarMarbetes(){
		
		Date fechaInv = new Date().parse("dd/MM/yyyy", params.fechaCierre)
		Integer marbeteInicial = params.marbeteInicial.toInteger()		
		def marbeteFinal = inventarioService.asignarMarbetes(fechaInv, marbeteInicial)
				
		render(contentType: 'text/json') {[marbeteFinal: marbeteFinal]}		
	}
	
	
	def checkInventario(){
		Date fecha = null
		try{
			fecha =  new Date().parse("dd/MM/yyyy",params.value)
		}
		catch(Exception e){
			fecha = null
		}
		
		def result = !inventarioService.checkInventario(fecha)
		render text: result, contentType:"text/html", encoding:"UTF-8"
	}
}
