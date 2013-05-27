package mx.gob.inr.utils.services

import groovy.sql.Sql
import javax.sql.DataSource
import mx.gob.inr.farmacia.*
import mx.gob.inr.utils.UtilService;
import mx.gob.inr.utils.reportes.*;
import mx.gob.inr.ceye.*

abstract class ReporteService {
		
	public DataSource dataSource    
    public UtilService utilService
	
	protected entityArticulo	
	
	public ReporteService(entityArticulo){
		this.entityArticulo = entityArticulo	
	}
	
	def reporteKardex(params){		
		
		String nameCierre, nameVista
		
		if(params.almacen == 'F'){
			nameCierre = "cierre"
			nameVista = "v_kardex"
		}
		else{
			nameCierre = "cierre_ceye"
			nameVista = "v_kardexceye"
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
		def fechaCierre = utilService.obtenerFechaCierre(fechaInicial)		
		
		
		def reporteKardexList = []
		
		for(articulo in articuloList){
			
			def query =
			""" 
				SELECT 	c.cve_art AS articulo, 
					   	c.fecha_cierre AS fecha, 
					   	0 AS folio, 
						('EXISTENCIA AL ' || fecha_cierre) AS procedencia, 
						c.existencia AS cantidad, 
						c.importe AS precio,
						'CIERRE' AS tipo, 
						almacen 
				FROM $nameCierre c 
				WHERE fecha_cierre = ?
				AND cve_art = $articulo.id 
				AND almacen = '$params.almacen' 
			UNION ALL 
				SELECT * 
				FROM $nameVista 
				WHERE articulo = $articulo.id  
				AND fecha BETWEEN ? AND ?   
				AND almacen = '$params.almacen' 
				ORDER BY fecha, folio 		
			"""
						
			db.eachRow(query,[new java.sql.Date(fechaCierre.getTime()),
				new java.sql.Date(fechaInicial.getTime()),new java.sql.Date(fechaFinal.getTime())]){
			
				def reporteKardex = new ReporteKardex(
					articulo:articulo,
					fecha:it.fecha,
					folio:it.folio,
					procedencia:it.procedencia,
					cantidad:it.cantidad,
					precio:it.precio,
					importe:it.cantidad * it.precio,
					tipo:it.tipo,
					almacen:it.almacen
					);
				
				reporteKardexList.add(reporteKardex)
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
				property("existencia")
				gt("fechaCierre",maximaFechaCierre)
				eq("articulo.id",articulo.id)
				eq("almacen",params.almacen)
			}
			
			if(!existenciaCierre)
				existenciaCierre = 0
			
			def sumaEntrada = sumaDetalle(entityEntradaDetalle,	entityCierre,params.almacen,"cantidad",articulo.id, "E")[0]			
			def sumaSalida = sumaDetalle(entitySalidaDetalle,entityCierre,params.almacen,"cantidadSurtida",articulo.id,"S")[0]
				
			def existencia = sumaEntrada + existenciaCierre - sumaSalida
						
			def reporteExistencia = new ReporteExistencia(articulo:articulo,existencia:existencia)		
			reporteExistenciaList.add(reporteExistencia)
			
		}
		
		reporteExistenciaList	
	}
	
	
	
	
	
	
	def reporteConsumo(params){
		
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
		
		def salidaDetalleList = entitySalidaDetalle.createCriteria().list(){
			
			salida{				
				area{
					if(params.area){
						eq("id",params.area.toLong())
					}
				}
								
				between("fecha",fechaInicial, fechaFinal)
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
			
			order("articulo","asc")
		}
		
		def reporteConsumoList = []
		
		def consumoMap = [:]		
		
		def mapMes = [1:'ene',2:'feb',3:'mar',4:'abr',5:'may',6:'jun',7:'jul',8:'ago',9:'sep',10:'oct',11:'nov',12:'dic']
		
		for(salidaDetalle in salidaDetalleList){		
			
			def articulo = salidaDetalle.articulo
			def mes  = mapMes[salidaDetalle.salida.fecha.month + 1]
			def cantidad = salidaDetalle.cantidadSurtida
			
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
			
			def reporteConsumo = reporteConsumoList.find { r -> r.articulo == key[0] }
			
			if(reporteConsumo){
				reporteConsumo."${key[1]}" = value
			}
			else{
				reporteConsumo = new ReporteConsumo(area:area)
				reporteConsumo.articulo = key[0]				
				reporteConsumo."${key[1]}" = value
				reporteConsumoList.add(reporteConsumo)		
			}
		}
		
		
		reporteConsumoList
	}
	
	
	private def sumaDetalle(entityDetalleAlmacen, entityCierre,
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
