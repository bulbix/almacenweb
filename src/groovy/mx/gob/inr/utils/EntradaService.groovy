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
	protected entityArticulo
	protected entityArea
	protected String almacen

	public EntradaService(entityEntrada, entityEntradaDetalle, entityArticulo, entityArea ,almacen){
		this.entityEntrada = entityEntrada
		this.entityEntradaDetalle = entityEntradaDetalle
		this.entityArticulo = entityArticulo
		this.entityArea = entityArea
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
		entrada.usuario = Usuario.get(jsonEntrada.registra)
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

		if(entrada.id){

			def e = entityEntradaDetalle.createCriteria()
			e.list(){
				eq("entrada", entrada)
			}*.delete()
		}

		entrada = guardar(entrada)

		Integer renglon = 1

		jsonArrayDetalle.each() {
			guardarDetalle(it, entrada, renglon++)
		}
	}

	@Override
	def actualizar(E entrada, Long idEntradaUpdate){

		def entradaUpdate = entityEntrada.get(idEntradaUpdate)

		if(entradaUpdate.folio != entrada.folio){
			if(checkFolio(entrada.folio)){
				return "Folio ya existe"
			}
		}

		entradaUpdate.properties = entrada.properties
		entradaUpdate.save([validate:false,flush:true])

		return "Entrada Actualizada"
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

		def fechas = utilService.fechasAnioActual()

		def entradaList = entityEntrada.createCriteria().list(params){
			between("fecha",fechas.fechaInicio,fechas.fechaFin)
			order("folio","desc")
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
		}

		[lista:entradaList, total:entradaTotal]
	}

	@Override
	def actualizarDetalle(Long idEntrada, params){

		def detalle = entityEntradaDetalle.createCriteria().get{
			eq('entrada.id', idEntrada)
			eq("articulo.id", params.long('id'))
			maxResults(1)
		}

		if(detalle){
			detalle.cantidad = params.double('cantidad') 
			detalle.precioEntrada = params.double('precioEntrada') 
			detalle.noLote = params.noLote
						
			try{
				detalle.fechaCaducidad = new Date().parse("dd/MM/yyyy",params.fechaCaducidad)
			}
			catch(Exception e){
				detalle.fechaCaducidad = null
			}
		}

		detalle.save([validate:false,flush:true])
	}

	@Override
	def borrarDetalle(Long idEntrada, Long clave){
		def detalle = entityEntradaDetalle.createCriteria().list(){
			eq('entrada.id', idEntrada)
			eq("articulo.id", clave)
		}*.delete()
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

}
