package mx.gob.inr.utils

import java.util.Date;

abstract class EntradaService<E extends Entrada> {

	public final int PERFIL_FARMACIA = 8
	public final int PERFIL_CEYE  = 10
	
	public UtilService utilService
	
	protected entityEntrada	
	protected entityEntradaDetalle
	protected entityArticulo
	
	public EntradaService(entityEntrada, entityEntradaDetalle, entityArticulo){
		this.entityEntrada = entityEntrada		
		this.entityEntradaDetalle = entityEntradaDetalle
		this.entityArticulo = entityArticulo
	}
	
	
	def guardarSalidaMaterial(jsonArrayEntrada,jsonArrayDetalle,ipTerminal){
			
		if(jsonArrayEntrada.idEntrada){
			entrada = entityEntrada.get(jsonArrayEntrada.idEntrada)
			
			def e = entityEntradaDetalle.createCriteria()
			e.list(){
				eq("entrada", entrada)
			}*.delete()
		}
		
		def entrada = entityEntrada.newInstance()
		
		entrada.numeroEntrada = Integer.parseInt(jsonArrayEntrada.folioEntrada)
		entrada.fechaEntrada = new Date().parse("dd/MM/yyyy",jsonArrayEntrada.fechaEntrada)
		
		log.info("ID salida almacen"  + jsonArrayEntrada.idSalAlma);
		
		if(jsonArrayEntrada.idSalAlma)
			entrada.idSalAlma = Integer.parseInt(jsonArrayEntrada.idSalAlma)
					
		entrada.numeroFactura = jsonArrayEntrada.remision
		entrada.usuario = Usuario.get(jsonArrayEntrada.registra)
		entrada.recibio = Usuario.get(jsonArrayEntrada.recibe)
		entrada.supervisor = Usuario.get(jsonArrayEntrada.supervisa)
		entrada.ipTerminal = ipTerminal
		entrada.presupuesto = null
		entrada.actividad = null
		
		entrada.save([validate:false,flush:true])
		
		def renglonEntrada = 0
		
		jsonArrayDetalle.each() {
			
			guardarEntradaDetalle(it, entrada)
		}
	}
		
	
	def guardar(E entrada){
		entrada.save([validate:false,flush:true])
		return entrada		
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
	
	
	def listar(){
		
	}
	
	
	def guardarDetalle(jsonEntradaDetalle, E entrada){		
		
		def articulo = entityArticulo.get(jsonEntradaDetalle.cveArt)		
		def entradaDetalle = entityEntradaDetalle.class.newInstance()
		
		entradaDetalle.entrada = entrada
		entradaDetalle.articulo articulo,
		entradaDetalle.cantidad = Double.parseDouble(jsonEntradaDetalle.cantidad),
		entradaDetalle.existencia = Double.parseDouble(jsonEntradaDetalle.cantidad),
		entradaDetalle.noLote = jsonEntradaDetalle.noLote,
		entradaDetalle.precioEntrada = Double.parseDouble(jsonEntradaDetalle.precioEntrada),
		entradaDetalle.renglonEntrada = consecutivoRenglon(entrada)		
		
		if(jsonEntradaDetalle.fechaCaducidad)
			entradaDetalle.fechaCaducidad =  new Date().parse("dd/MM/yyyy",jsonEntradaDetalle.fechaCaducidad)
					
		entradaDetalle.save([validate:false,flush:true])
		
	}
	
	def actualizarDetalle(){
		
	}
	
	def borrarDetalle(){
		
	}
	
	
	def consultarDetalle(params){
		
		def sortIndex = params.sidx ?: 'name'
		def sortOrder  = params.sord ?: 'asc'
		def maxRows = Integer.valueOf(params.rows)
		def currentPage = Integer.valueOf(params.page) ?: 1
		def rowOffset = currentPage == 1 ? 0 : (currentPage - 1) * maxRows
		
		log.info("IDENTRADA " + params.idEntrada)
		
		def detalleCount = entityEntradaDetalle.createCriteria().list(){
			eq('entrada.id',Long.parseLong(params.idEntrada))
		}
		
		def detalle = entityEntradaDetalle.createCriteria().list(max: maxRows, offset: rowOffset) {
			eq('entrada.id',Long.parseLong(params.idEntrada))
			order(sortIndex, sortOrder)
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
	
	
}
