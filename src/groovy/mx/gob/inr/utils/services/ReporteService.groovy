package mx.gob.inr.utils.services

import groovy.sql.Sql
import javax.sql.DataSource
import mx.gob.inr.farmacia.*
import mx.gob.inr.utils.UtilService;
import mx.gob.inr.utils.reportes.*;
import mx.gob.inr.ceye.*
import mx.gob.inr.utils.*;
import mx.gob.inr.materiales.SalidaMaterial;

abstract class ReporteService {
		
	public DataSource dataSource    
    public UtilService utilService
	
	protected entityArticulo	
	
	public ReporteService(entityArticulo){
		this.entityArticulo = entityArticulo	
	}
	
	def reporteKardex(params){
		
		def nameVista = "v_kardex", entityCierre = CierreFarmacia		
		
		if(params.almacen != 'F'){
			nameVista = "v_kardexceye"
			entityCierre = CierreCeye
		}
		
		def claveInicial = params.long('claveInicial')
		def claveFinal = params.long('claveFinal')
		def fechaInicial = new Date().parse("dd/MM/yyyy", params.fechaInicial)
		def fechaFinal = new Date().parse("dd/MM/yyyy", params.fechaFinal)
		
		def articuloList = entityArticulo.createCriteria().list(){
			
			partida{
				if(params.partida){
					eq("partida",params.partida)
				}
			}
						
						
			between("id",claveInicial, claveFinal)
			order("id","asc")
		}
		
		def db = new Sql(dataSource)
		
		def reporteKardexList = []
		
		for(articulo in articuloList){
			
			def cierre = utilService.cierreAnterior(entityCierre,fechaInicial,articulo,params.almacen)
			
			if(cierre){
				
				//cierre.fechaCierre = new java.util.Date(cierre.fechaCierre.getTime())
				
				reporteKardexList << [articulo:articulo,fecha:cierre.fechaCierre,folio:0,procedencia:"(EXISTENCIA AL ${cierre.fechaCierre.format('dd/MM/yyyy')})",
				cantidad:cierre.existencia,precio:cierre.importe,importe:(cierre.existencia*cierre.importe),tipo:'CIERRE',almacen:params.almacen]
			}
			
			def query =
			"""
				SELECT * 
				FROM $nameVista 
				WHERE articulo = $articulo.id  
				AND fecha BETWEEN ? AND ?   
				AND almacen = '$params.almacen' 
				ORDER BY fecha, folio 		
			"""
						
			db.eachRow(query,[new java.sql.Date(fechaInicial.getTime()),new java.sql.Date(fechaFinal.getTime())]){
				
				def procedencia = it.procedencia
				
				if(procedencia.startsWith('ALMACEN/FARMACIA')){
					def idSalAlma = procedencia.substring(procedencia.indexOf("(")+1, procedencia.indexOf(")"))					
					def numeroSalida = SalidaMaterial.get(idSalAlma)?.folio					
					procedencia = 'ALMACEN/FARMACIA (' + numeroSalida + ')'
				}
							
				reporteKardexList << [articulo:articulo,fecha:it.fecha,folio:it.folio,procedencia:procedencia,
				cantidad:it.cantidad,precio:it.precio,importe:(it.cantidad*it.precio),tipo:it.tipo,almacen:params.almacen]
			}
		}
		
		reporteKardexList
	}
	
	
	def reporteExistencia(params){
		
		def entityCierre, entityEntradaDetalle, entitySalidaDetalle
		
		if(params.almacen == 'F'){
			entityEntradaDetalle = EntradaDetalleFarmacia
			entitySalidaDetalle = SalidaDetalleFarmacia
			entityCierre = CierreFarmacia			
		}
		else{
			entityEntradaDetalle = EntradaDetalleCeye
			entitySalidaDetalle = SalidaDetalleCeye
			entityCierre = CierreCeye			
		}		
		
		
		def claveInicial = params.long('claveInicial')
		def claveFinal = params.long('claveFinal')
		
		def maximaFechaCierre = utilService.maximaFechaCierre(entityCierre, params.almacen)		
		
		def articuloList = entityArticulo.createCriteria().list(){
			partida{
				if(params.partida){
					eq("partida",params.partida)
				}			
			}
			between("id",claveInicial, claveFinal)
			order("id","asc")
		}
		
		def reporteExistenciaList = []
		
		for(articulo in articuloList){		
			
			def existenciaCierre = entityCierre.createCriteria().get{
				projections{
					property("existencia")
				}
				eq("fechaCierre",maximaFechaCierre)
				eq("articulo.id",articulo.id)
				eq("almacen",params.almacen)
			}
			
			if(!existenciaCierre)
				existenciaCierre = 0
			
			def sumaEntrada = sumaDetalle(entityEntradaDetalle,	entityCierre,params.almacen,"cantidad",articulo.id, "E")[0]			
			def sumaSalida = sumaDetalle(entitySalidaDetalle,entityCierre,params.almacen,"cantidadSurtida",articulo.id,"S")[0]
				
			def existencia = sumaEntrada + existenciaCierre - sumaSalida	
					
			reporteExistenciaList << [articulo:articulo, existencia:existencia]
			
		}
		
		reporteExistenciaList	
	}
		
	def reporteProporcionado(params){
		def entitySalidaDetalle,entityArea
		
		if(params.almacen == 'F'){
			entitySalidaDetalle = SalidaDetalleFarmacia
			entityArea = CatAreaFarmacia
		}
		else{
			entitySalidaDetalle = SalidaDetalleCeye
			entityArea = CatAreaCeye
		}
		
		def claveInicial = params.long('claveInicial')
		def claveFinal = params.long('claveFinal')
		def fechaInicial = new Date().parse("dd/MM/yyyy", params.fechaInicial)
		def fechaFinal = new Date().parse("dd/MM/yyyy", params.fechaFinal)
		
		//params.fechaInicial = fechaInicial
		//params.fechaFinal = fechaFinal
		
		def reporteProporcionadoList = []
		
		def salidaDetalleList = entitySalidaDetalle.createCriteria().list(){
			
			projections{
				groupProperty("salida")			
				groupProperty("articulo")
				groupProperty("cantidadPedida")//Hay error
				sum("cantidadSurtida")
			}
			
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
				
				between("id",claveInicial, claveFinal)		
			}	
			
			order("articulo","asc")
		}.each() {salidaDetalle ->
				
			reporteProporcionadoList << [articulo:salidaDetalle[1],cantidadPedida:salidaDetalle[2],cantidadSurtida:salidaDetalle[3]]		
		}		
		
		reporteProporcionadoList
		
	}
		
	def reporteConsumo(params){
		params.reporteName = 'REPORTE CONSUMOS'		
		reporteMes(params,"S")
	}
		
	def reporteEntrada(params){
		params.reporteName = 'REPORTE ENTRADAS'
		reporteMes(params,'E')
	}
		
	def reportePartidaEntrada(params){
		params.reporteName = 'REPORTE ENTRADAS POR PARTIDA'
		reportePartida(params,'e')				
	}
		
	def reportePartidaSalida(params){
		params.reporteName = 'REPORTE SALIDAS POR PARTIDA'
		reportePartida(params,'S')
	}
		
	protected def reportePartida(params,String tipo){
		
		def entityDetalle
		
		if(params.almacen == 'F'){
			entityDetalle = tipo=='S'? SalidaDetalleFarmacia: EntradaDetalleFarmacia			
		}
		else{
			entityDetalle = tipo=='S'? SalidaDetalleCeye: EntradaDetalleCeye			
		}
		
		def fechaInicial = new Date().parse("dd/MM/yyyy", params.fechaInicial)
		def fechaFinal = new Date().parse("dd/MM/yyyy", params.fechaFinal)
		
		def reporteList = []
		
		def presupuestoMap = [federal:['p',''],cuota:['c','r'],otro:['o']]
		
		def partidaMap = [:]
		
		presupuestoMap.each(){ key,value->			
			
			def detalleList = entityDetalle.createCriteria().list(){
				
				projections{
					sum("importe")//formula
				}
				
				if(tipo == 'S'){										
					salida{						
						between("fecha",fechaInicial, fechaFinal)
						eq("estado","A")
						eq("almacen",params.almacen)
					}
										
					if(key == 'federal'){
						or{
							isNull("presupuesto")
							'in'("presupuesto", value)
						}
					}
					else{
						'in'("presupuesto", value)
					}
				}
				else{					
					entrada{
						between("fecha",fechaInicial, fechaFinal)
						eq("estado","A")
						eq("almacen",params.almacen)
						
						if(key == 'federal'){
							or{
								isNull("presupuesto")
								'in'("presupuesto", value)
							}
						}
						else{
							'in'("presupuesto", value)
						}
					}
				}
							
				articulo{
					partida{
						groupProperty("partida")
						order("partida","asc")
					}
				}
			}.each(){		
				
				def reportePartida = reporteList.find { r -> r.partida == it[1] }
				
				if(reportePartida){
					reportePartida."${key}" += it[0]
				}
				else{
					reportePartida = new ReportePartida()
					reportePartida.partida = Partida.findWhere(partida:it[1])
					reportePartida."${key}" += it[0]
					reporteList << reportePartida
				}		
				
			}			
		}
		
		reporteList	
		
		
	}
		
	protected def reporteMes(params, String tipo){
		
		def entityDetalle,entityArea
				
		if(params.almacen == 'F'){
			entityDetalle = tipo=='S'? SalidaDetalleFarmacia: EntradaDetalleFarmacia
			entityArea = CatAreaFarmacia
		}
		else{
			entityDetalle = tipo=='S'? SalidaDetalleCeye: EntradaDetalleCeye
			entityArea = CatAreaCeye
		}
		
		def claveInicial = params.long('claveInicial')
		def claveFinal = params.long('claveFinal')
		def fechaInicial = new Date().parse("dd/MM/yyyy", params.fechaInicial)
		def fechaFinal = new Date().parse("dd/MM/yyyy", params.fechaFinal)
		
		//params.fechaInicial = fechaInicial
		//params.fechaFinal = fechaFinal
		
		def numMeses = fechaFinal[Calendar.MONTH]-fechaInicial[Calendar.MONTH]+1
		
		
		def detalleList = entityDetalle.createCriteria().list(){
			
			
			if(tipo == 'S'){
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
			}
			else{
				entrada{
					between("fecha",fechaInicial, fechaFinal)
					eq("estado","A")
					eq("almacen",params.almacen)
				}
				
			}
			
			
			articulo{
				partida{
					if(params.partida){
						eq("partida",params.partida)
					}
				}
				between("id",claveInicial, claveFinal)
			}
			
			order("articulo","asc")			
		}
		
		def reporteList = []
		
		def consumoMap = [:]
		
		def mapMes = [1:'ene',2:'feb',3:'mar',4:'abr',5:'may',6:'jun',7:'jul',8:'ago',9:'sep',10:'oct',11:'nov',12:'dic']
		
		for(detalle in detalleList){
			
			def articulo = detalle.articulo
			
			def mes = 0, cantidad  = 0
			
			if(tipo == 'S'){
				mes  = mapMes[detalle.salida.fecha.month + 1]
				cantidad = detalle.cantidadSurtida				
			}
			else{
				mes  = mapMes[detalle.entrada.fecha.month + 1]
				cantidad = detalle.cantidad				
			}
			
			if(consumoMap.containsKey([articulo,mes])) {
				def cantidadMap = consumoMap.get([articulo,mes])
				consumoMap.put([articulo,mes], cantidadMap + cantidad )
			}
			else{
				consumoMap.put([articulo,mes], cantidad )
			}
		}
		
		def area = entityArea.get(params.area)
		
		consumoMap.each(){key,value ->
			
			
			
			def reporteMes = reporteList.find { r -> r.articulo == key[0] }
			
			if(reporteMes){
				reporteMes."${key[1]}" = value
			}
			else{
				reporteMes = new ReporteMes(area:area,numMeses:numMeses)
				reporteMes.articulo = key[0]
				reporteMes.articulo.movimientoProm  = utilService.getMovimientoPromedio(key[0],params.almacen)
				reporteMes."${key[1]}" = value
				reporteList << reporteMes
			}
		}
		
		
		reporteList
		
	}

	protected def sumaDetalle(entityDetalleAlmacen, entityCierre,
		String almacen, String cantidad, Long clave, String tipo){
		
		def maximaFechaCierre = utilService.maximaFechaCierre(entityCierre, almacen)	
		
		def sum = entityDetalleAlmacen.createCriteria().get{
			projections{
				sum(cantidad)
				
			}
			
			articulo{
				eq("id", clave)
				groupProperty("id")
			}
			
			if(tipo=="E"){
				entrada{
					gt("fecha", maximaFechaCierre)
					eq("estado","A")
					eq("almacen",almacen)
				}
			}
			else{
				salida{
					gt("fecha", maximaFechaCierre)
					eq("estado","A")
					eq("almacen",almacen)
				}			
			}
		}
		
		if(!sum)
			sum = [0,0]
			
			
		return sum
		
	}
	
}
