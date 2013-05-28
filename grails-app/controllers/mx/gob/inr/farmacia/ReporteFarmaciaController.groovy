package mx.gob.inr.farmacia

import javax.annotation.PostConstruct;
import mx.gob.inr.utils.ReporteController

class ReporteFarmaciaController extends ReporteController {

	def reporteFarmaciaService
	
	public ReporteFarmaciaController(){
		super(ArticuloFarmacia,"F")
	}
	
	@PostConstruct
	def init(){
		reporteService = reporteFarmaciaService
	}
	
	def reporteCaducidad(){
		cargarParams()
	}
	
}
