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
	
	public reporteConvertidora(params){
		def reporteList = []
		reporteList		
	}
	
	public reporteExistenciaConjunto(params){
		
		
		def fechaInicial = new Date().parse("dd/MM/yyyy", params.fechaInicial)
		def fechaFinal = new Date().parse("dd/MM/yyyy", params.fechaFinal)
		
		params.fechaCierreCeye = utilService.maximaFechaCierre(CierreCeye,'C').format('yyyy-MM-dd')
		params.fechaCierreSubceye = utilService.maximaFechaCierre(CierreCeye,'S').format('yyyy-MM-dd')
		params.fechaCierreCeniaq = utilService.maximaFechaCierre(CierreCeye,'Q').format('yyyy-MM-dd')
		params.fechaInicial = fechaInicial.format('yyyy-MM-dd')
		params.fechaFinal =  fechaFinal.format('yyyy-MM-dd')
		
		def reporteList = []
		reporteList
	}
	
}
