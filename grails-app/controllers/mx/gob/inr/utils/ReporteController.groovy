package mx.gob.inr.utils

import mx.gob.inr.farmacia.*
import mx.gob.inr.ceye.*
import mx.gob.inr.ceye.reportes.ReporteDynamicConcentradoServicios;

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
	
	def reporteConcentradoServicioSalida(){
		cargarParams()
	}
	
	/*****
	 * Genera los reportes echos en jasper
	 * @return
	 */
	def reporte() {
		
		def methodName = params.methodName
		
		params.IMAGE_DIR = "${servletContext.getRealPath('/images')}/"
		params.SUBREPORT_DIR = "${servletContext.getRealPath('/reports')}/"
		params.SUBREPORT_CEYE_DIR = "${servletContext.getRealPath('/reports')}/ceye/"
		params.locale =  new Locale("es","MX");
		params.almacen = session.almacen

		def data = reporteService."$methodName"(params) //Llamada dinamica del metodo
		chain(controller: "jasper", action: "index", model: [data:data], params:params)
		
	}
	
	/****
	 * Genera los reportes hechos en itext
	 * @return
	 */
	/*def reporteItext(){	
		
		def imageDir = "${servletContext.getRealPath('/images')}/"
		
		def rango = "${params.reportDisplay} DEL " + params.fechaInicial + " AL " + params.fechaFinal	
		
		ReporteConcentradoServicios reporteServicio = new ReporteConcentradoServicios(utilService,imageDir)		

		def data = reporteService."${params.methodName}"(params) //Llamada dinamica del metodo
		
		def areaList = CatAreaCeye.findAllByAlmacen(params.almacen, [sort: "id", order: "asc"])		
		
		byte[] bytes = reporteServicio.generarReporte(data, rango, areaList,params.almacen,params.tipoVale)
		def datos =  new ByteArrayInputStream(bytes)
		utilService.mostrarReporte(response, datos,'application/pdf',"${params.reportDisplay}.pdf")			
		
	}*/
	
	/****
	 * Reportes Generados con Dynamic Reports
	 * @return
	 */
	def reporteDynamic(){		
		def imageDir = "${servletContext.getRealPath('/images')}/"
		ReporteDynamicConcentradoServicios reporteServicio =
		 new ReporteDynamicConcentradoServicios(utilService,imageDir,response)
		def data = reporteService."${params.methodName}"(params)
		reporteServicio.generarReporte(data,params)		
	}
	
	
}
