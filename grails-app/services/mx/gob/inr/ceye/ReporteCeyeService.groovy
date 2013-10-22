package mx.gob.inr.ceye

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import mx.gob.inr.utils.UtilService;
import mx.gob.inr.utils.reportes.ReporteSala
import mx.gob.inr.utils.services.ReporteService;

class ReporteCeyeService extends ReporteService {

	static transactional = true
	
	DataSource dataSource
	UtilService utilService
	DataSource dataSourceMateriales	
	
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
		
		
		//def fechaInicial = new Date().parse("dd/MM/yyyy", params.fechaInicial)
		//def fechaFinal = new Date().parse("dd/MM/yyyy", params.fechaFinal)
		
		params.fechaCierreCeye = utilService.maximaFechaCierre(CierreCeye,'C').format('yyyy-MM-dd')
		params.fechaCierreSubceye = utilService.maximaFechaCierre(CierreCeye,'S').format('yyyy-MM-dd')
		params.fechaCierreCeniaq = utilService.maximaFechaCierre(CierreCeye,'Q').format('yyyy-MM-dd')
		//params.fechaInicial = fechaInicial.format('yyyy-MM-dd')
		//params.fechaFinal =  fechaFinal.format('yyyy-MM-dd')
		
		def reporteList = []
		reporteList
	}
	
	public reporteSala(params){
		
		def entityDetalle = SalidaDetalleCeye 
		def entityArea = CatAreaCeye
		
		def fechaInicial = new Date().parse("dd/MM/yyyy", params.fechaInicial)
		def fechaFinal = new Date().parse("dd/MM/yyyy", params.fechaFinal)

		def detalleList = entityDetalle.createCriteria().list(){	
			salida{
				area{
					if(params.area){
							eq("id",params.area.toLong())
					}
				}
									
				between("fecha",fechaInicial, fechaFinal)
				eq("estado","A")
				eq("almacen",params.almacen)
			}
			
			articulo{
				partida{
					if(params.partida){
						eq("partida",params.partida)
					}
				}
			}
			
			order("articulo","asc")
		}
	
	


		def reporteList = []

		def salaMap = [:]

		def mapNumero = [1:'uno',2:'dos',3:'tres',4:'cuatro',5:'cinco',6:'seis',7:'siete',8:'ocho',9:'nueve',10:'diez']

		for(detalle in detalleList){
	
			def articulo = detalle.articulo
	
			def sala = 0, cantidad  = 0
	
			if(detalle.salida.nosala){
				sala  = mapNumero[detalle.salida.nosala as int]
				cantidad = detalle.cantidadSurtida
		
				if(salaMap.containsKey([articulo,sala])) {
					def cantidadMap = salaMap.get([articulo,sala])
					salaMap.put([articulo,sala], cantidadMap + cantidad )
					}
				else{
					salaMap.put([articulo,sala], cantidad )
				}				
			}
		}

		def area = entityArea.get(params.area)

		salaMap.each(){key,value ->
	
			def reporteSala = reporteList.find { r -> r.articulo == key[0] }
			
			if(reporteSala){
				reporteSala."${key[1]}" = value
			}
			else{
				reporteSala = new ReporteSala(area:area)
				reporteSala.articulo = key[0]
				//reporteSala.articulo.movimientoProm  = utilService.getMovimientoPromedio(key[0],params.almacen)
				reporteSala."${key[1]}" = value
				reporteList << reporteSala
			}
		}


		reporteList
		
	}
	
	
	public reporteFoliosAlmacen(params){
		
		def fechaInicial = new Date().parse("dd/MM/yyyy", params.fechaInicial)
		def fechaFinal = new Date().parse("dd/MM/yyyy", params.fechaFinal)
		
		params.fechaInicial = fechaInicial.format('yyyy-MM-dd')
		params.fechaFinal =  fechaFinal.format('yyyy-MM-dd')		 
		params.SUBREPORT_CONEXION = dataSource?.getConnection()
		//params.REPORT_CONNECTION=  dataSourceMateriales?.getConnection()
		
		def reporteList = []
		reporteList
	}
	
}
