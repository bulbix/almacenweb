package mx.gob.inr.farmacia

import javax.annotation.PostConstruct;
import mx.gob.inr.utils.ReporteController
import grails.plugins.springsecurity.Secured;

@Secured(['ROLE_FARMACIA'])
class ReporteFarmaciaController extends ReporteController {

	def reporteFarmaciaService
	
	public ReporteFarmaciaController(){
		super(ArticuloFarmacia)
	}
	
	@PostConstruct
	def init(){
		reporteService = reporteFarmaciaService
	}
	
	def reporteCaducidad(){
		cargarParams()
	}
	
	def reporteDetalladoSalida(){
		cargarParams()
	}
	
	def reporteSurtimiento(){
		cargarParams()
	}
	
}
