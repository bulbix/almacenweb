package mx.gob.inr.utils

import mx.gob.inr.farmacia.*
import mx.gob.inr.ceye.*

class ReporteController {
	
	def utilService
	def reporteService
	
	protected String almacen
	protected entityArticulo
	
	public ReporteController(entityArticulo,almacen){
		this.entityArticulo = entityArticulo
		this.almacen = almacen
	}
	
	def index() {
	
	}
	
	def cargarParams(){		
		def fechaInicial = new Date()
		def fechaFinal = new Date()
		def claveInicial = utilService.clave(entityArticulo,"min")
		def claveFinal =  utilService.clave(entityArticulo,"max")
		def partidaList = Partida.listOrderByDesPart()
		
		def areaList
		
		if(almacen == 'F')
			areaList = CatAreaFarmacia.listOrderByDesArea()
		else
			areaList = CatAreaCeye.listOrderByDesArea()
			
		
		[fechaInicial:fechaInicial,fechaFinal:fechaFinal,
		 claveInicial:claveInicial,claveFinal:claveFinal,
		 partidaList:partidaList,areaList:areaList, almacen:almacen]	
	}
	
	def reporteKardex(){
		cargarParams()
	}
	
	def reporteExistencia(){
		cargarParams()
	}
	
	def reporteConsumo(){
		cargarParams()
	}
	
	
	def reporte() {
		
		def methodName = params.reportName		
		
		try{			
			//params.SUBREPORT_DIR = "${servletContext.getRealPath('/reports')}/"
			
			params.IMAGE_DIR = "${servletContext.getRealPath('/images')}/"		
			def data = reporteService."$methodName"(params) //Llamada dinamica del metodo 			
			chain(controller: "jasper", action: "index", model: [data:data], params:params)
		}
		catch(Exception e){
			flash.message= 'Revise sus parametros'
			redirect(action:methodName,params:params)
			return		
		}
	}
	
	
}
