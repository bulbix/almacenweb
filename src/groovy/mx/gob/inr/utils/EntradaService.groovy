package mx.gob.inr.utils

import grails.converters.JSON
import java.util.Date;

abstract class EntradaService<E extends Entrada> {

	public final int PERFIL_FARMACIA = 8
	public final int PERFIL_CEYE  = 10
	
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
	
	E setJsonEntrada(jsonEntrada,  String ip){
		
		def entrada = entityEntrada.newInstance()
		entrada.almacen = almacen
		entrada.ipTerminal = ip
		
		entrada.numeroEntrada = jsonEntrada.folioEntrada as int
		entrada.fechaEntrada = new Date().parse("dd/MM/yyyy",jsonEntrada.fechaEntrada)
		
		if(jsonEntrada.idSalAlma)
			entrada.idSalAlma = jsonEntrada.idSalAlma as int
					
		entrada.numeroFactura = jsonEntrada.remision
		entrada.usuario = Usuario.get(jsonEntrada.registra)
		entrada.recibio = Usuario.get(jsonEntrada.recibe)
		entrada.supervisor = Usuario.get(jsonEntrada.supervisa)		
		entrada.presupuesto = null
		entrada.actividad = null
		
		return entrada
	}
	
	def guardarSalidaMaterial(E entrada, jsonDetalle){
			
		if(entrada.idEntrada){
									
			def e = entityEntradaDetalle.createCriteria()
			e.list(){
				eq("entrada", entrada)
			}*.delete()
		}
		
		entrada.save()
		
		jsonDetalle.each() {			
			guardarDetalle(it, entrada)
		}
	}
		
	def guardar(E entrada){
		entrada.save([validate:false,flush:true])
		return entrada		
	}
	
	def guardarDetalle(jsonDetalle, E entrada){
		
		def articulo = entityArticulo.get(jsonDetalle[0].cveArt)
		
		def entradaDetalle = entityEntradaDetalle.newInstance()
				
		entradaDetalle.entrada = entrada
		entradaDetalle.articulo = articulo
		entradaDetalle.cantidad = jsonDetalle[0].cantidad as double
		entradaDetalle.existencia = jsonDetalle[0].cantidad as double
		entradaDetalle.noLote = jsonDetalle[0].noLote
		entradaDetalle.precioEntrada = jsonDetalle[0].precioEntrada as double
		entradaDetalle.renglonEntrada = consecutivoRenglon(entrada)
		
		if(jsonDetalle[0].fechaCaducidad)
			entradaDetalle.fechaCaducidad =  new Date().parse("dd/MM/yyyy",jsonDetalle[0].fechaCaducidad)
					
		entradaDetalle.save([validate:false,flush:true])
		
	}
	
	def actualizar(E entrada, Integer idEntradaUpdate){
		
	}
	
	def consultar(Integer idEntrada){
		def entrada =  entityEntrada.get(idEntrada)
		if(entrada.idSalAlma)
			entrada.folioAlmacen = SalidaMaterial.get(entrada.idSalAlma).numeroSalida;
		return entrada
		
	}
	
	def cancelar(){
		
	}
	
	
	def listar(params){
		
		def fechas = utilService.fechasAnioActual()
		
		def entradaList = entityEntrada.createCriteria().list(params){	
			between("fechaEntrada",fechas.fechaInicio,fechas.fechaFin)
			order("numeroEntrada","desc")
		}
		
		entradaList.each(){
			if(it.idSalAlma)
				it.folioAlmacen = SalidaMaterial.get(it.idSalAlma).numeroSalida;
		}

		def entradaTotal = entityEntrada.createCriteria().get{
			projections{
				count()
			}
			between("fechaEntrada",fechas.fechaInicio,fechas.fechaFin)
		}

		[entradaList:entradaList, entradaTotal:entradaTotal]
	}
			
	def actualizarDetalle(Long idEntrada, Long clave, Double cantidad, Double precio, String noLote, Date fechaCaducidad){
		
		def detalle = entityEntradaDetalle.createCriteria().get{
			eq('entrada.id', idEntrada)
			eq("articulo.id", clave)
			maxResults(1)
		}
		
		if(detalle){
			detalle.cantidad = cantidad
			detalle.precioEntrada = precio
			detalle.noLote = noLote
			detalle.fechaCaducidad = fechaCaducidad
		}
		
		detalle.save()
	}
	
	def borrarDetalle(Long idEntrada, Long clave){
		def detalle = entityEntradaDetalle.createCriteria().list(){
			eq('entrada.id', idEntrada)
			eq("articulo.id", clave)
		}*.delete()
	}	
	
	def consultarDetalle(params){
		
		def sortIndex = params.sidx ?: 'id'
		def sortOrder  = params.sord ?: 'asc'
		def maxRows = Integer.valueOf(params.rows)
		def currentPage = Integer.valueOf(params.page) ?: 1
		def rowOffset = currentPage == 1 ? 0 : (currentPage - 1) * maxRows		
		def idEntrada  = params.long('idEntrada')
		
		log.info("IDENTRADA " + params.idEntrada)
		
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
					 it.noLote,it.fechaCaducidad?.format('dd/MM/yyyy')], id: it.renglonEntrada
			]
		}
		
		def jsonData = [rows: results, page: currentPage, records: totalRows, total: numberOfPages]
		return jsonData
		
	}
	
	def consultaMaterial (Integer folioAlmacen, Integer cveArea){
		
		def query = """\
			select sd from SalidaDetalleMat sd join fetch sd.id.salida s 
			where s.numeroSalida = :folio and s.fechaSalida between :fecha1 and :fecha2 and s.almacen = 'F' 
			and s.cveArea = :area and s.estadoSalida <> 'C' order by sd.articulo
		"""
		
		def fechas = utilService.fechasAnioActual()
		
		def detalle  = SalidaDetalleMaterial.executeQuery(query,
			[folio:folioAlmacen,fecha1:fechas.fechaInicio,fecha2:fechas.fechaFin, area:cveArea])
		
		def idSalAlma = null
		
		if(detalle.size() > 0)
			idSalAlma = detalle[0].salida.id
			
		log.info("ID salida almacen "  + detalle[0].salida.id);
		
		
		def results = detalle?.collect {
			[
				cveArt:it.articulo.id,desArticulo:it.articulo.desArticulo?.trim(),unidad:it.articulo.unidad?.trim(),
				cantidad:it.cantidadSurtida,precioEntrada:it.precioUnitario,noLote:it.noLote,
				fechaCaducidad:it.fechaCaducidad?.format('dd/MM/yyyy')
			]
		}
		
		return [rows:results,idSalAlma:idSalAlma]
	}
		
	def consecutivoRenglon(E entrada){
		utilService.consecutivoRenglon(entityEntradaDetalle,"renglonEntrada","entrada",entrada)
	}
	
	def checkFolioEntrada(Integer folioEntrada){
		
		utilService.checkFolio(entityEntrada,"numeroEntrada","fechaEntrada", folioEntrada)
		
	}
	
	def checkFolioSalAlma(Integer folioSalAlma){
		
		def fechas = utilService.fechasAnioActual()
				
		def criteriaSalida  = SalidaMaterial.createCriteria();
		
		def resultSalida = criteriaSalida.get(){
			eq("numeroSalida",folioSalAlma)
			between("fechaSalida",fechas.fechaInicio,fechas.fechaFin)
			eq("almacen","F")
		}
		
		def queryEntrada = entityEntrada.where {
			idSalAlma == resultSalida?.id
		}
		
		def resultEntrada = queryEntrada.find()
		
		if(resultEntrada)
			return true
		else
			return false
	}
	
	def consecutivoNumeroEntrada(){		
		utilService.consecutivoNumero(entityEntrada, "numeroEntrada","fechaEntrada")
	}	
	
	def disponibilidadArticulo(Long clave, Date fecha, String almacen){
		
		def criteria = entityEntradaDetalle.createCriteria()
		
		def disponible = criteria.get {
			
			projections {
				sum("existencia")
			}
			
			entrada {
				eq("almacen", almacen)
				eq("estadoEntrada","A")
				le("fechaEntrada",fecha)
			}
			
			articulo {
				eq("id",clave)
			}
		}
		
		if(!disponible)
			return 0
		else
			return disponible
	}
	
	def entradasDetalle(Long clave, Date fecha, String almacen){
		
		def criteria = entityEntradaDetalle.createCriteria()
		Double cero = 0.0
		
		def detalle = criteria.list() {
						
			entrada {
				eq("almacen", almacen)
				eq("estadoEntrada","A")
				le("fechaEntrada",fecha)
			}
			
			articulo {
				eq("id",clave)
			}
			
			ne("existencia",cero)
			
			order("fechaCaducidad", "asc")
		}
		
		detalle
	}
	
	def usuarios(Integer idPerfil){
		return utilService.usuarios(idPerfil)	
	}
	
	def buscarArticulo(Long id){
		def articulo = entityArticulo.get(id)
		articulo.desArticulo = articulo.desArticulo.trim()
		return articulo
	}
	
	def listarArticulo(String term){
		 autoCompleteService.listarArticulo(term, entityArticulo)
	}
	
	def listarArea(String term){
		autoCompleteService.listarArea(term, entityArea)
	}
	
	
}
