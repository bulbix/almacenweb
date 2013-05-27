package mx.gob.inr.ceye
import javax.annotation.PostConstruct;

import mx.gob.inr.utils.ReporteController;

class ReporteCeyeController extends ReporteController {

	def reporteCeyeService
	
    public ReporteCeyeController(){
		super(ArticuloCeye,"C")
	}
	
	@PostConstruct
	def init(){
		reporteService = reporteCeyeService
	}
}
