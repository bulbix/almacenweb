package mx.gob.inr.utils

import mx.gob.inr.farmacia.*
import mx.gob.inr.ceye.*

class ReporteController {
	
	def utilService
	def reporteService
		
	protected entityArticulo
	
	public ReporteController(entityArticulo){
		this.entityArticulo = entityArticulo		
	}
	
	def index() {
	
	}
	
	def cargarParams(){		
		def fechaInicial = utilService.fechaPrimeroMes()
		def fechaFinal = new Date()
		def claveInicial = utilService.clave(entityArticulo,"min")
		def claveFinal =  utilService.clave(entityArticulo,"max")
		def partidaList = Partida.listOrderByDesPart()
		
		def areaList
		
		if(params.almacen){
			session.almacen = params.almacen
		}
		
		if(session.almacen == 'F')
			areaList = CatAreaFarmacia.listOrderByDesArea()
		else
			areaList = CatAreaCeye.listOrderByDesArea()
			
		
		[fechaInicial:fechaInicial,fechaFinal:fechaFinal,
		 claveInicial:claveInicial,claveFinal:claveFinal,
		 partidaList:partidaList,areaList:areaList, almacen:session.almacen]	
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
	
	def reporteProporcionado(){
		cargarParams()
	}
	
	def reporteEntrada(){
		cargarParams()
	}
	
	def reportePartidaSalida(){
		cargarParams()
	}
	
	def reportePartidaEntrada(){
		cargarParams()
	}
	
	
	def reporte() {
		
		def methodName = params.methodName		
		
		//try{			
			
			params.IMAGE_DIR = "${servletContext.getRealPath('/images')}/"
			params.SUBREPORT_DIR = "${servletContext.getRealPath('/reports')}/"
			params.SUBREPORT_CEYE_DIR = "${servletContext.getRealPath('/reports')}/ceye/"
			params.locale =  new Locale("es","MX");
			params.almacen = session.almacen
					
			def data = reporteService."$methodName"(params) //Llamada dinamica del metodo 			
			chain(controller: "jasper", action: "index", model: [data:data], params:params)
		/*}
		catch(Exception e){
			flash.message= 'Revise sus parametros'
			redirect(action:methodName,params:params)
			return		
		}*/
	}
	
	
}
