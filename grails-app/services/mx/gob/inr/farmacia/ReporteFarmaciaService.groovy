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
	
	def reporteDetalladoSalida(params){
		def fechaInicial = new Date().parse("dd/MM/yyyy", params.fechaInicial)
		def fechaFinal = new Date().parse("dd/MM/yyyy", params.fechaFinal)	
		
		params.fechaInicial = fechaInicial.format('yyyy-MM-dd')
		params.fechaFinal =  fechaFinal.format('yyyy-MM-dd')
		
		def reporteList = []
		reporteList
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

	
	def reporteSurtimiento(params){
		
		def fechaInicial = new Date().parse("dd/MM/yyyy", params.fechaInicial)
		def fechaFinal = new Date().parse("dd/MM/yyyy", params.fechaFinal)
		
		def reporteList = []
		
		params.fechaInicial = fechaInicial.format('yyyy-MM-dd')
		params.fechaFinal =  fechaFinal.format('yyyy-MM-dd')
		
		int numPaciente = 0;
		int numStock = 0;
		
		def salidaList = SalidaFarmacia.createCriteria().list(){			
			area{				
			}	
			
			between("fecha",fechaInicial, fechaFinal)			
			order("fecha","asc")
			order("folio","asc")
			
		}.each { salida ->
			int noClaveSolicitadas = 0;
			int noClavesCompletas = 0;
			int noClavesIncompletas = 0;
			int noClavesCero = 0;
			
			def detalleList = SalidaDetalleFarmacia.createCriteria().list(){
				eq("salida.id", salida.id)
			}
			
			if(salida.tipoSolicitud){
				if(salida.tipoSolicitud.equals("paciente")){
					numPaciente++;
				}
				else if(salida.tipoSolicitud.equals("stock")){
					numStock++;
				}
			}
			
			for(detalle in detalleList){
				noClaveSolicitadas++;
				
				if(detalle.cantidadPedida == detalle.cantidadSurtida)
					noClavesCompletas++;
				else if((detalle.cantidadPedida != detalle.cantidadSurtida) && detalle.cantidadSurtida != 0)
					noClavesIncompletas++;
				else
					noClavesCero++;
			}
			
			reporteList << [fechaSalida: salida.fecha, folioSalida:salida.folio,area:salida.area, noClavesSolicitadas:noClaveSolicitadas,
				noClavesCompletas:noClavesCompletas,noClavesIncompletas:noClavesIncompletas,
				noClavesCero:noClavesCero,tipoSolicitud:"",numPaciente:0,numStock:0]	
		}
		
		for(surtimiento in reporteList){
			surtimiento['numPaciente'] = numPaciente
			surtimiento['numStock'] = numStock
		}
		
		reporteList
		
	}
   
}
