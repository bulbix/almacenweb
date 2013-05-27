package mx.gob.inr.utils.services

import java.util.Date;

import mx.gob.inr.farmacia.ArticuloFarmacia;
import mx.gob.inr.farmacia.CierreFarmacia;
import mx.gob.inr.utils.UtilService;
import mx.gob.inr.utils.domain.Articulo;
import mx.gob.inr.utils.domain.Cierre;

abstract class CierreService <C extends Cierre, A extends Articulo> {
	
	public UtilService utilService
	
	protected entityEntrada
	protected entitySalida
	protected entitySalidaDetalle
	protected entityArticulo
	protected entityCierre	
	protected String almacen	
	
	public int valueProgress = 0
	
	
	public CierreService(entityEntrada, entitySalida, entitySalidaDetalle, entityArticulo ,entityCierre, almacen){
		this.entityEntrada  = entityEntrada
		this.entitySalida = entitySalida
		this.entitySalidaDetalle = entitySalidaDetalle
		this.entityArticulo = entityArticulo
		this.entityCierre = entityCierre
		this.almacen = almacen
	}	
	
	def cierreAnterior(Date fechaCierre, A articulo){
		
		def mFechaCierre = utilService.fechaDesglosada(fechaCierre)
		def fechaCierreAnterior = new Date()
		
		if(mFechaCierre.mes == 0){
			Calendar cal = Calendar.getInstance();
			cal.set(mFechaCierre.anio - 1 , 11 , 1)//01/12/anio ANterior
			fechaCierreAnterior  = cal.getTime();
		}
		else{
			Calendar cal = Calendar.getInstance();
			cal.set(mFechaCierre.anio , mFechaCierre.mes -1 , 1) //01/mes ANterior/anio Cierre
			fechaCierreAnterior  = cal.getTime();
		}
		
		def mFechaCierreAnterior = utilService.fechaDesglosada(fechaCierreAnterior)
		
		def cierre = entityCierre.createCriteria().get{
			sqlRestriction("month(fecha_cierre) = ($mFechaCierreAnterior.mes + 1)")
			sqlRestriction("year(fecha_cierre) = $mFechaCierreAnterior.anio")
			eq("articulo",articulo)
			eq("almacen", almacen)
		}
		
		return cierre
	}
	
	def concentrarEntradaSalida(Date fechaCierre, A articulo){
	
		def mFechaCierre  = utilService.fechaDesglosada(fechaCierre)
		
		Calendar cal = Calendar.getInstance();
		cal.set(mFechaCierre.anio, mFechaCierre.mes, 1)
		def fechaCierreDiaPrimero  = cal.getTime();
		
		def listaConcentradora = []
	
		def entityEntradaName = entityEntrada.name
				
		def strQueryEntrada = """
        select new mx.gob.inr.utils.ConcentradoraCierre(e.fecha,'E',ed.cantidad,ed.precioEntrada,e.id,e.almacen) 
		from $entityEntradaName e inner join e.entradasDetalle ed 
		where ed.articulo = :articulo and e.fecha between :fechaInicio and :fechaFin 
		and e.estado <> 'C' and e.almacen = '$almacen' 
		""" 
			
		listaConcentradora.addAll(entityEntrada.executeQuery(strQueryEntrada,
			["articulo":articulo,"fechaInicio":fechaCierreDiaPrimero,"fechaFin":fechaCierre]))
		
		def entitySalidaName = entitySalida.name
		
		def strQuerySalida = """
        select new mx.gob.inr.utils.ConcentradoraCierre(s.fecha,'S',sd.cantidadSurtida,sd.precioUnitario,s.id,s.almacen) 
		from $entitySalidaName s inner join s.salidasDetalle sd 
		where sd.articulo = :articulo and s.fecha between :fechaInicio and :fechaFin 
		and s.estado <> 'C' and s.almacen = '$almacen' 
		""" 
			
		listaConcentradora.addAll(entitySalida.executeQuery(strQuerySalida,
			["articulo":articulo,"fechaInicio":fechaCierreDiaPrimero,"fechaFin":fechaCierre]))
		
		Collections.sort(listaConcentradora)
		log.info(listaConcentradora)
		return listaConcentradora
	}
	
	def calcularAjusteCierre(listaConcentradora, Date fechaCierre, C cierreAnterior, A articulo){
		
		Long sumaExistAnt = 0;
		Long sumaSalidaAnt = 0;
		
		Double nuevoCostoPromedio = cierreAnterior.importe;
		
		listaConcentradora.each { concentra ->
			
			switch(concentra.tipo){
				
				case 'E':
				
					Long existEntradaActual = concentra.cantidad
					Double importeEntradaActual = concentra.costoPromedio
				
					Long sumExistAnt = cierreAnterior.existencia - sumaSalidaAnt + sumaExistAnt;
					
					Double totalExist = existEntradaActual * importeEntradaActual;
					Double totalExistAcum =  sumExistAnt * nuevoCostoPromedio;//costoAnteriorMes;
					
					if((sumExistAnt + existEntradaActual) > 0)
					{
						nuevoCostoPromedio = (totalExist + totalExistAcum) / (sumExistAnt + existEntradaActual);
					}
					else
					{
						nuevoCostoPromedio = costoAnteriorMes;
					}
					
					/**Se mete al acumulado la nueva existencia de entradas*/
					sumaExistAnt += existEntradaActual;
				
					break;
				
				case 'S':
				
					sumaSalidaAnt += concentra.cantidad
					
					def salidaDetalle = entitySalidaDetalle.createCriteria().get{
						eq("salida.id", concentra.llave )
						eq("articulo", articulo )
					}
					
					salidaDetalle.precioUnitario = nuevoCostoPromedio
					
					salidaDetalle.save()
					
					break;
					
			}
		}
		
		long nuevaExistencia = cierreAnterior.existencia + sumaExistAnt - sumaSalidaAnt;
		
		def cierre = entityCierre.newInstance()
		
		cierre.fechaCierre = fechaCierre
		cierre.almacen = this.almacen
		cierre.articulo = articulo
		cierre.existencia = nuevaExistencia
		cierre.importe = nuevoCostoPromedio
		
		
		return cierre
		
	}
	
	def cerrarPeriodo(Date fechaCierre){
		
		int minClave = utilService.clave(entityArticulo,"min")
		int maxClave = utilService.clave(entityArticulo,"max")

		int totalArticulos = (maxClave - minClave);
		int contadorArticulo = 0;
		
		for(clave in minClave..maxClave){
			
			def articulo  = entityArticulo.get(clave)
			
			if(articulo){
			
				def listaConcentradora = concentrarEntradaSalida(fechaCierre, articulo)
				def cierreAnterior  = cierreAnterior(fechaCierre, articulo)
				
				if(!cierreAnterior){
					
					cierreAnterior = entityCierre.newInstance()
					cierreAnterior.existencia = 0
					cierreAnterior.importe = 0.0
				}
				
							
				def cierreNuevo = calcularAjusteCierre(listaConcentradora,fechaCierre, cierreAnterior, articulo)
				
				cierreNuevo.save()
				
				articulo.movimientoProm = 	cierreNuevo.importe
				
				articulo.save()
			}
			
			//Calculamos el porcentaje
			double porcentaje = (contadorArticulo * 100.0) / (totalArticulos * 1.0);
			++contadorArticulo;
			valueProgress = (long)porcentaje;
			
		}
	}
	
	def checkCierre(Date fecha){
		utilService.checkCierre(entityCierre, fecha, almacen)
	}
	
	def listar(){
		
		def fechas = utilService.fechasAnioActual()
				
		def cierreList = entityCierre.createCriteria().list(){			
			projections{
				distinct("fechaCierre")
			}
			
			eq("almacen",almacen)
			between("fechaCierre",fechas.fechaInicio,fechas.fechaFin)
			order("fechaCierre","asc")
		}
		
		def cierreTotal = entityCierre.createCriteria().get{
			projections{
				countDistinct("fechaCierre")
			}
			
			eq("almacen",almacen)
			between("fechaCierre",fechas.fechaInicio,fechas.fechaFin)
		}
		
		[lista:cierreList, total:cierreTotal]
	}
	
	def checkCierrePosterior(Date fecha){	
		
		def fechaDesglosada = utilService.fechaDesglosada(fecha)		
		
		def mes = fechaDesglosada.mes
		def anio  = fechaDesglosada.anio
		
		if(mes == 11){
			mes = 0
			++anio
		}
				
		for(imes in mes..11){
			def fechaNext = new Date()
									
			fechaNext.set(month:(imes+1),year:anio)
			if(checkCierre(fechaNext)){
				return true
			}
		}	
		
		return false
		
	}
	
	def eliminar(Date fecha){
		
		if(!checkCierrePosterior(fecha)){		
			def cierreList = entityCierre.createCriteria().list(){
				eq("fechaCierre", fecha)
				eq("almacen",almacen)			
			}*.delete()
			
			return true
		}
		
		return false
	}
	
	def reporte(Date fechaCierre){
		def entityCierreName = entityCierre.name	
						
		def query =
		"""	select c from $entityCierreName c 
			left join fetch c.articulo art 
			left join fetch art.partida p  
			where c.fechaCierre = ? 
			and c.almacen = ? 
			and c.existencia > 0 
			order by art asc
		"""	
		
		def cierreList = entityCierre.executeQuery(query,[fechaCierre,almacen])
				
		return cierreList
		
	}
	
	
}
