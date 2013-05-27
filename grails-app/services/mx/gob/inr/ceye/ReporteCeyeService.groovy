package mx.gob.inr.ceye

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import mx.gob.inr.utils.UtilService;
import mx.gob.inr.utils.services.ReporteService;

class ReporteCeyeService extends ReporteService {

	static transactional = true
	
	DataSource dataSource
	UtilService utilService
	
	public ReporteCeyeService(){
		super(ArticuloCeye)
	}
	
	
	
	@PostConstruct
	public void init(){
		super.dataSource = dataSource
		super.utilService =  utilService
	}
	
}
