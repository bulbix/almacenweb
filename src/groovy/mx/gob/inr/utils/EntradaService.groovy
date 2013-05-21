package mx.gob.inr.utils

import grails.converters.JSON
import java.util.Date;
import mx.gob.inr.materiales.*
import mx.gob.inr.farmacia.*
import mx.gob.inr.ceye.*

abstract class EntradaService<E extends Entrada> implements IOperacionService<E> {

	public UtilService utilService
	public AutoCompleteService autoCompleteService

	protected entityEntrada
	protected entityEntradaDetalle
	protected entitySalidaDetalle
	protected entityArticulo
	protected entityArea
	protected entityCierre
	protected String almacen

	public EntradaService(entityEntrada, entityEntradaDetalle, entitySalidaDetalle, entityArticulo, entityArea, entityCierre, almacen){
		this.entityEntrada = entityEntrada
		this.entityEntradaDetalle = entityEntradaDetalle
		this.entitySalidaDetalle = entitySalidaDetalle
		this.entityArticulo = entityArticulo
		this.entityArea = entityArea
		this.entityCierre = entityCierre
		this.almacen = almacen
	}

	@Override
	E setJson(jsonEntrada,  String ip){

		def entrada = entityEntrada.newInstance()
		entrada.almacen = almacen
		entrada.ipTerminal = ip
		entrada.folio = jsonEntrada.folio as int
		entrada.fecha = new Date().parse("dd/MM/yyyy",jsonEntrada.fecha)

		if(jsonEntrada.idSalAlma)
			entrada.idSalAlma = jsonEntrada.idSalAlma as int

		entrada.numeroFactura = jsonEntrada.remision
		entrada.usuario = Usuario.get(6558)
		entrada.recibio = Usuario.get(jsonEntrada.recibe)
		entrada.supervisor = Usuario.get(jsonEntrada.supervisa)
		entrada.presupuesto = null
		entrada.actividad = null


		if(entrada instanceof EntradaFarmacia){
			entrada.devolucion  = jsonEntrada.devolucion == 'on'?'1':'0'
		}
		else if(entrada instanceof EntradaCeye){
			entrada.area =  entityArea.get(jsonEntrada.cveArea)
			entrada.paqueteq = jsonEntrada.paqueteq
		}


		return entrada
	}

	@Override
	E guardar(E entrada){
		entrada.save([validate:false,flush:true])
		return entrada
	}

	@Override
	def guardarDetalle(jsonDetalle, E entrada, Integer renglon){

		def articulo = entityArticulo.get(jsonDetalle.cveArt)

		def entradaDetalle = entityEntradaDetalle.newInstance()

		entradaDetalle.entrada = entrada
		entradaDetalle.articulo = articulo
		entradaDetalle.cantidad = jsonDetalle.cantidad as double
		entradaDetalle.existencia = jsonDetalle.cantidad as double
		entradaDetalle.noLote = jsonDetalle.noLote
		entradaDetalle.precioEntrada = jsonDetalle.precioEntrada as double

		if(!renglon)
			entradaDetalle.renglon = consecutivoRenglon(entrada)
		else
			entradaDetalle.renglon = renglon


		try{
			entradaDetalle.fechaCaducidad =  new Date().parse("dd/MM/yyyy",jsonDetalle.fechaCaducidad)
		}
		catch(Exception e){
			entradaDetalle.fechaCaducidad = null
		}

		entradaDetalle.save([validate:false,flush:true])

	}

	@Override
	def guardarTodo(E entrada, jsonArrayDetalle){
		
		entrada = guardar(entrada)

		Integer renglon = 1

		jsonArrayDetalle.each() {
			guardarDetalle(it, entrada, renglon++)
		}
		
		return "Entrada Guardada"
	}

	@Override
	def actualizar(E entrada, Long idEntradaUpdate){
	
		def entradaUpdate = entityEntrada.get(idEntradaUpdate)
		def mensaje = "Entrada actualizada";
		
		if(entrada.fecha.compareTo(entradaUpdate.fecha) > 0){
			if(existeSalida(idEntradaUpdate)){
				def formatFecha = entradaUpdate.fecha.format('dd/MM/yyyy')
				mensaje= "Fecha actualizable <= $formatFecha"
				entrada.fecha = entradaUpdate.fecha
			}			
		}
		
		if(entrada.folio != entradaUpdate.folio){
			if(checkFolio(entrada.folio)){
				mensaje = "Folio no actualizable, ya existe"
				entrada.folio = entradaUpdate.folio
			}
		}

		entradaUpdate.properties = entrada.properties
		
		entradaUpdate.save([validate:false,flush:true])

		return mensaje
	}

	@Override
	def consultar(Long idEntrada){
		def entrada =  entityEntrada.get(idEntrada)
		if(entrada.idSalAlma)
			entrada.folioAlmacen = SalidaMaterial.get(entrada.idSalAlma).folio;
		return entrada

	}

	@Override
	def cancelar(Long idEntrada){

	}

	@Override
	def listar(params){
		
		def sortIndex = params.sort ?: 'folio'
		def sortOrder  = params.order ?: 'desc'

		
		def fechas = utilService.fechasAnioActual()

		def entradaList = entityEntrada.createCriteria().list(params){
			between("fecha",fechas.fechaInicio,fechas.fechaFin)
			eq("almacen",almacen)
			order(sortIndex, sortOrder)
		}

		entradaList.each(){
			if(it.idSalAlma)
				it.folioAlmacen = SalidaMaterial.get(it.idSalAlma).folio;
		}

		def entradaTotal = entityEntrada.createCriteria().get{
			projections{
				count()
			}
			between("fecha",fechas.fechaInicio,fechas.fechaFin)
			eq("almacen",almacen)
		}

		[lista:entradaList, total:entradaTotal]
	}

	@Override
	def actualizarDetalle(Long idEntrada, params){

		
		def clave = params.long('id')
		def cantidad = params.double('cantidad')
		def precioEntrada = params.double('precioEntrada') 
		
		def detalle = entityEntradaDetalle.createCriteria().get{
			eq('entrada.id', idEntrada)
			eq("articulo.id", clave)
			maxResults(1)
		}

		if(detalle){
			
			if(precioEntrada == null || precioEntrada < 0.0){
				return "Precio Invalido"
			}
			
			if(cantidad ==null || cantidad < 0.0){
				return "Cantidad Invalida"
			}
			else{								
				def salidasDetalle = salidasDetalle(idEntrada, clave)

				if(salidasDetalle){
					def sumaSurtido = salidasDetalle.sum { it.cantidadSurtida }
					if(cantidad < sumaSurtido)
						return "Cantidad utilizada $suma"
				}
			}				
			
			detalle.existencia += (cantidad - detalle.cantidad)
			detalle.cantidad = cantidad
			detalle.precioEntrada = precioEntrada
			detalle.noLote = params.noLote
						
			try{
				if(params.fechaCaducidad)
					detalle.fechaCaducidad = new Date().parse("dd/MM/yyyy",params.fechaCaducidad)
			}
			catch(Exception e){
				return "Formato fecha caducidad invalido"
				detalle.fechaCaducidad = null
			}
		}

		detalle.save([validate:false,flush:true])
		
		return "success"
	}

	@Override
	def borrarDetalle(Long idEntrada, Long clave){
		
		def salidaDetalle = salidasDetalle(idEntrada,clave)
		
		if(salidaDetalle.size() != 0){
			return "Clave ya tiene salida"
		}
		
		def detalle = entityEntradaDetalle.createCriteria().list(){
				eq('entrada.id', idEntrada)
				eq("articulo.id", clave)
			}*.delete()	
		
		return "success"
	}

	@Override
	def consultarDetalle(params){

		def sortIndex = params.sidx ?: 'id'
		def sortOrder  = params.sord ?: 'asc'
		def maxRows = Integer.valueOf(params.rows)
		def currentPage = Integer.valueOf(params.page) ?: 1
		def rowOffset = currentPage == 1 ? 0 : (currentPage - 1) * maxRows
		def idEntrada  = params.long('idPadre')

		log.info("IDENTRADA " + params.idPadre)

		def detalleCount = entityEntradaDetalle.createCriteria().list(){
			eq('entrada.id',idEntrada)
		}

		def criteria = entityEntradaDetalle.createCriteria()

		def detalle = criteria.list(max: maxRows, offset: rowOffset) {
			eq('entrada.id',idEntrada)
			//order(sortIndex, sortOrder)
		}

		def totalRows = detalleCount.size();
		def numberOfPages = Math.ceil(totalRows / maxRows)

		def results = detalle?.collect {
			[
				cell:[it.articulo.id,it.articulo.desArticulo?.trim(),
					it.articulo.unidad?.trim(),it.cantidad,it.precioEntrada,
					it.noLote,it.fechaCaducidad?.format('dd/MM/yyyy')], id: it.articulo.id
			]
		}

		def jsonData = [rows: results, page: currentPage, records: totalRows, total: numberOfPages]
		return jsonData

	}

	@Override
	def consecutivoRenglon(E entrada){
		utilService.consecutivoRenglon(entityEntradaDetalle,"entrada",entrada)
	}

	@Override
	def checkFolio(Integer folio){

		utilService.checkFolio(entityEntrada, folio,almacen)

	}

	@Override
	def consecutivoFolio(){
		utilService.consecutivoFolio(entityEntrada, almacen)
	}
	
	@Override
	def usuarios(Integer idPerfil){
		return utilService.usuarios(idPerfil)
	}

	@Override
	def buscarArticulo(Long id){
		def articulo = entityArticulo.get(id)
		articulo.desArticulo = articulo.desArticulo.trim()
		return articulo
	}

	@Override
	def listarArticulo(String term){
		autoCompleteService.listarArticulo(term, entityArticulo)
	}

	@Override
	def listarArea(String term){
		autoCompleteService.listarArea(term, entityArea)
	}
	
	@Override
	def checkCierre(Date fecha){
		utilService.checkCierre(entityCierre, fecha, almacen)
	}
	
	@Override
	def reporte(Long id){
		
		def entityEntradaDetalleName = entityEntradaDetalle.name
		
		def area = ""
		
		if(almacen != 'F')
			area = " left join fetch e.area "
					
		def query =
		"""
			select ed from $entityEntradaDetalleName ed
			left join fetch ed.entrada e 
			left join fetch e.recibio 
			left join fetch e.supervisor
			left join fetch e.usuario
			$area
			left join fetch ed.articulo art 
			where e.id = $id 
			order by ed.renglon asc
		"""	
		
		def detalleList = entityEntradaDetalle.executeQuery(query,[])
				
		if(almacen == 'F'){		
			def folioAlmacen = null
			if(detalleList){				
				if(detalleList[0].entrada.idSalAlma)
					folioAlmacen= SalidaMaterial.get(detalleList[0].entrada.idSalAlma).folio;
			}
			for(detalle in detalleList){
				detalle.entrada.folioAlmacen = folioAlmacen				
			}
		}
		
		return detalleList		
	}

	def checkFolioSalAlma(Integer folioSalAlma){

		def fechas = utilService.fechasAnioActual()

		def criteriaSalida  = SalidaMaterial.createCriteria();

		def resultSalida = criteriaSalida.get(){
			eq("folio",folioSalAlma)
			between("fecha",fechas.fechaInicio,fechas.fechaFin)
			eq("cveArea",AREA_FARMACIA) 
			eq("almacen","F")
		}

		def result = entityEntrada.createCriteria().get {
			eq("idSalAlma",resultSalida?.id as int)
		}
		
		if(result)
			return true
		else
			return false
	}
	
	def entradasDetalle(Long clave, Date fecha, String almacen){

		def criteria = entityEntradaDetalle.createCriteria()
		Double cero = 0.0

		def detalle = criteria.list() {

			entrada {
				eq("almacen", almacen)
				eq("estado","A")
				le("fecha",fecha)
			}

			articulo {
				eq("id",clave)
			}

			ne("existencia",cero)

			order("fechaCaducidad", "asc")
		}

		detalle
	}
	
	def existeSalida(Long idEntrada){
		def criteria = entitySalidaDetalle.createCriteria()
		
		def result = criteria.get{
			entradaDetalle{
				eq('entrada.id', idEntrada)
				 maxResults(1)				
			}
		}
		
		if(result)
			return true
		else
			return false	
	}
	
	def salidasDetalle(Long idEntrada, Long clave){
		
		def criteria = entitySalidaDetalle.createCriteria()
		
		def detalle = criteria.list(){
			entradaDetalle{
				eq('entrada.id', idEntrada)
				eq("articulo.id", clave)
			}
		}
		
		detalle
		
	}
	
	
	

}
