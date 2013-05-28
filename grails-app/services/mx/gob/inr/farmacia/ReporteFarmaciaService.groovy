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
	
	def reporteCaducidad(params){	
		
		def claveInicial = params.long('claveInicial')
		def claveFinal = params.long('claveFinal')
		def fechaInicial = new Date().parse("dd/MM/yyyy", params.fechaInicial)
		def fechaFinal = new Date().parse("dd/MM/yyyy", params.fechaFinal)
		
		def reporteList = []
		
		def detalleList = EntradaDetalleFarmacia.createCriteria().list(){
			
			projections{
				groupProperty("fechaCaducidad")
				groupProperty("articulo")
				sum("existencia")
			}	
			
			entrada{					
				eq("estado","A")
			}		
			
			articulo{
				partida{
					if(params.partida){
						eq("partida",params.partida)
					}
				}
				between("id",claveInicial, claveFinal)
			}
			
			between("fechaCaducidad",fechaInicial, fechaFinal)
			
			order("articulo","asc")
		}.each(){
			reporteList << [fechaCaducidad:it[0],articulo:it[1],existencia:it[2]]
		}
		
		reporteList
		
	}

   
}
