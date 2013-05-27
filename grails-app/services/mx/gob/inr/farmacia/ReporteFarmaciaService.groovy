package mx.gob.inr.farmacia

import javax.annotation.PostConstruct;
import javax.sql.DataSource

import mx.gob.inr.utils.UtilService
import mx.gob.inr.utils.services.ReporteService;

class ReporteFarmaciaService extends ReporteService{
	
	static transactional = true
	
	DataSource dataSource
	UtilService utilService
	
	public ReporteFarmaciaService(){
		super(ArticuloFarmacia)
	}
	
	@PostConstruct
	public void init(){
		super.dataSource = dataSource
		super.utilService =  utilService
	} 

   
}
