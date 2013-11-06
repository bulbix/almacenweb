package mx.gob.inr.utils.services

import java.util.Date;

import mx.gob.inr.ceye.CierreCeye
import mx.gob.inr.ceye.CostoPromedioCeye;
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
		
	
	public int valueProgress = 0
	
	
	public CierreService(entityEntrada, entitySalida, entitySalidaDetalle, entityArticulo ,entityCierre){
		this.entityEntrada  = entityEntrada
		this.entitySalida = entitySalida
		this.entitySalidaDetalle = entitySalidaDetalle
		this.entityArticulo = entityArticulo
		this.entityCierre = entityCierre		
	}	
	
	
	
	def concentrarEntradaSalida(Date fechaCierre, A articulo,String almacen){
	
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
	
	def calcularAjusteCierre(listaConcentradora, Date fechaCierre, C cierreAnterior, A articulo,String almacen){
		
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
						nuevoCostoPromedio = cierreAnterior.importe;
					}
					
					/**Se mete al acumulado la nueva existencia de entradas*/
					sumaExistAnt += existEntradaActual;
				
					break;
				
				case 'S':
				
					sumaSalidaAnt += concentra.cantidad
					
					def salidaDetalle = entitySalidaDetalle.createCriteria().list{
						eq("salida.id", concentra.llave )
						eq("articulo", articulo )
					}.each{					
						it.precioUnitario = nuevoCostoPromedio					
						it.save([validate:false])
					}
					
					break;
					
			}
		}
		
		long nuevaExistencia = cierreAnterior.existencia + sumaExistAnt - sumaSalidaAnt;
		
		def cierre = entityCierre.newInstance()
		
		cierre.fechaCierre = fechaCierre
		cierre.almacen = almacen
		cierre.articulo = articulo
		cierre.existencia = nuevaExistencia
		cierre.importe = nuevoCostoPromedio
		
		
		return cierre
		
	}
	
	def cerrarPeriodo(Date fechaCierre, String almacen){
		
		int minClave = utilService.clave(entityArticulo,"min")
		int maxClave = utilService.clave(entityArticulo,"max")

		int totalArticulos = (maxClave - minClave);
		int contadorArticulo = 0;
		
		for(clave in minClave..maxClave){
			
			def articulo  = entityArticulo.get(clave)
			
			if(articulo){
			
				def listaConcentradora = concentrarEntradaSalida(fechaCierre, articulo, almacen)
				def cierreAnterior  = utilService.cierreAnterior(entityCierre,fechaCierre, almacen, articulo)
							
				def cierreNuevo = calcularAjusteCierre(listaConcentradora,fechaCierre, cierreAnterior, articulo,almacen)
				
				cierreNuevo.save([validate:false])				
				utilService.setMovimientoPromedio(articulo,cierreNuevo.importe,almacen)			
				
			}
			
			//Calculamos el porcentaje
			double porcentaje = (contadorArticulo * 100.0) / (totalArticulos * 1.0);
			++contadorArticulo;
			valueProgress = (long)porcentaje;
			
		}
	}
	
	def checkCierre(Date fecha,String almacen){
		utilService.checkCierre(entityCierre, fecha, almacen)
	}
	
	def listar(String almacen){
		
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
	
	def checkCierrePosterior(Date fecha, String almacen){	
		
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
			if(checkCierre(fechaNext,almacen)){
				return true
			}
		}	
		
		return false
		
	}
	
	def eliminar(Date fecha,String almacen){
		
		def entityCierreName = entityCierre.name
		
		if(!checkCierrePosterior(fecha,almacen)){
			entityCierre.executeUpdate("delete $entityCierreName c where c.fechaCierre = ? and almacen = ?", [fecha,almacen])
			return true
		}
		
		return false
	}
	
	def reporte(Date fechaCierre, String almacen){
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
