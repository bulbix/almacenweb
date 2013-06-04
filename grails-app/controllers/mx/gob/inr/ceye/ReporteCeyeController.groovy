package mx.gob.inr.ceye
import javax.annotation.PostConstruct;
import mx.gob.inr.utils.ReporteController;
import grails.plugins.springsecurity.Secured;

@Secured(['ROLE_CEYE'])
class ReporteCeyeController extends ReporteController {

	def reporteCeyeService
	
    public ReporteCeyeController(){
		super(ArticuloCeye,"C")
	}
	
	@PostConstruct
	def init(){
		reporteService = reporteCeyeService
	}
	
	def reporteConvertidora(){
		[almacen:almacen]
	}
	
	def reporteExistenciaConjunto(){
		cargarParams()
	}
}
