package mx.gob.inr.utils.services

import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import java.util.Date;
import mx.gob.inr.materiales.*
import mx.gob.inr.utils.AutoCompleteService;
import mx.gob.inr.seguridad.Usuario;
import mx.gob.inr.utils.UtilService;
import mx.gob.inr.utils.domain.Articulo;
import mx.gob.inr.utils.domain.Entrada;
import mx.gob.inr.farmacia.*
import mx.gob.inr.ceye.*
import mx.gob.inr.utils.Paciente

abstract class EntradaService<E extends Entrada> implements IOperacionService<E> {

	public UtilService utilService
	public AutoCompleteService autoCompleteService	

	protected entityEntrada
	protected entityEntradaDetalle
	protected entitySalidaDetalle
	protected entityArticulo
	protected entityArea
	protected entityCierre	

	public EntradaService(entityEntrada, entityEntradaDetalle, entitySalidaDetalle, entityArticulo, entityArea, entityCierre){
		this.entityEntrada = entityEntrada
		this.entityEntradaDetalle = entityEntradaDetalle
		this.entitySalidaDetalle = entitySalidaDetalle
		this.entityArticulo = entityArticulo
		this.entityArea = entityArea
		this.entityCierre = entityCierre
	}

	@Override
	E setJson(jsonEntrada,  String ip, Usuario usuarioRegistro, String almacen){

		def entrada = entityEntrada.newInstance()
		entrada.almacen = almacen
		entrada.ipTerminal = ip
		entrada.folio = jsonEntrada.folio as int
		entrada.fecha = new Date().parse("dd/MM/yyyy",jsonEntrada.fecha)

		if(jsonEntrada.idSalAlma)
			entrada.idSalAlma = jsonEntrada.idSalAlma as int
		
		entrada.usuario = usuarioRegistro
		entrada.recibio = Usuario.get(jsonEntrada.recibe)
		entrada.supervisor = Usuario.get(jsonEntrada.supervisa)
		entrada.presupuesto = null
		entrada.actividad = null


		if(entrada instanceof EntradaFarmacia){
			entrada.numeroFactura = jsonEntrada.remision
			entrada.devolucion  = jsonEntrada.devolucion == 'on'?'1':'0'
		}
		else if(entrada instanceof EntradaCeye){
			entrada.area =  entityArea.get(jsonEntrada.cveArea)
			entrada.paciente = Paciente.get(jsonEntrada.idPaciente)
			entrada.tipoVale = jsonEntrada.tipoVale
			entrada.paqueteq = jsonEntrada.paqueteq
		}


		return entrada
	}

	@Override
	E guardar(E entrada){
		entrada.save([validate:false])
		return entrada
	}

	@Override
	def guardarDetalle(jsonDetalle, E entrada, Integer renglon,String almacen){

		def articulo = entityArticulo.get(jsonDetalle.cveArt)

		def entradaDetalle = entityEntradaDetalle.newInstance()

		entradaDetalle.entrada = entrada
		entradaDetalle.articulo = articulo
		entradaDetalle.cantidad = jsonDetalle.cantidad as double
		entradaDetalle.existencia = jsonDetalle.cantidad as double
		
		entradaDetalle.precioEntrada = jsonDetalle.precioEntrada as double

		if(!renglon)
			entradaDetalle.renglon = consecutivoRenglon(entrada)
		else
			entradaDetalle.renglon = renglon

		if(entradaDetalle instanceof EntradaDetalleFarmacia){
			entradaDetalle.noLote = jsonDetalle.noLote	
			try{
				entradaDetalle.fechaCaducidad =  new Date().parse("dd/MM/yyyy",jsonDetalle.fechaCaducidad)
			}
			catch(Exception e){
				entradaDetalle.fechaCaducidad = new Date()
			}
		}
		else if(entradaDetalle instanceof EntradaDetalleCeye){		
			
			//Validacion por si viene de un paquete
			if(jsonDetalle.solicitadoFarmacia)						
				entradaDetalle.solicitadoFarmacia = jsonDetalle.solicitadoFarmacia as int
			else
				entradaDetalle.solicitadoFarmacia = jsonDetalle.solicitado as int 
			
			if(jsonDetalle.cantidadFarmacia)
				entradaDetalle.cantidadFarmacia  = jsonDetalle.cantidadFarmacia as int
			else
				entradaDetalle.cantidadFarmacia  = jsonDetalle.cantidad as int
			
			entradaDetalle.cantidadSolicitada = jsonDetalle.solicitado as double 			
			entradaDetalle.noLote = null
			entradaDetalle.fechaCaducidad = null
		}
		

		

		this.getCostoPromedio(entradaDetalle,almacen)
		
		entradaDetalle.save([validate:false])
	}

	@Override
	def guardarTodo(E entrada, jsonArrayDetalle, String almacen){
		
		entrada = guardar(entrada)

		Integer renglon = 1

		jsonArrayDetalle.each() {
			guardarDetalle(it, entrada, renglon++,almacen)
		}
		
		return "Entrada Guardada"
	}

	@Override
	def actualizar(E entrada, Long idEntradaUpdate,String almacen=null){
	
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
			if(checkFolio(entrada.folio,almacen)){
				mensaje = "Folio no actualizable, ya existe"
				entrada.folio = entradaUpdate.folio
			}
		}

		entradaUpdate.properties = entrada.properties
		
		entradaUpdate.save([validate:false])

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
	def cancelar(Long idEntrada,String almacen){
		
		def entrada =  entityEntrada.get(idEntrada);
		
		def salidasDetalle = salidasDetalle(idEntrada)
		
		log.info("SON salidas" + salidasDetalle)
		
		if(!salidasDetalle){		
			cancelarTodoCostoPromedio(entrada,almacen)
			entrada.estado = 'C'
			entrada.save([validate:false])
			return "Cancelado"
		}
		
		return "Folio tiene salidas asociadas"

	}

	@Override
	def listar(params, Usuario usuarioLogueado){
		
		def sortIndex = params.sort ?: 'folio'
		def sortOrder  = params.order ?: 'desc'

		
		def fechas = utilService.fechasAnioActual()

		def entradaList = entityEntrada.createCriteria().list(params){
			between("fecha",fechas.fechaInicio,fechas.fechaFin)
			eq("almacen",params.almacen)
			order(sortIndex, sortOrder)
		}

		entradaList.each(){
			if(it.idSalAlma)
				it.folioAlmacen = SalidaMaterial.get(it.idSalAlma)?.folio;
			
			it.dueno = usuarioLogueado == it.usuario
		}

		def entradaTotal = entityEntrada.createCriteria().get{
			projections{
				count()
			}
			between("fecha",fechas.fechaInicio,fechas.fechaFin)
			eq("almacen",params.almacen)
		}

		[lista:entradaList, total:entradaTotal]
	}

	@Override
	def actualizarDetalle(Long idEntrada, params,String almacen){

		
		def clave = params.long('id')
		def cantidad = params.double('cantidad')		
		def precioEntrada = params.double('precioEntrada')
		
		def solicitado = params.double('solicitado')
		
		def detalle = entityEntradaDetalle.createCriteria().get{
			entrada { eq('id', idEntrada)}
			articulo {eq("id", clave)}
			maxResults(1)
		}

		if(detalle){		
			
			//Para el modulo de CEYE
			if(detalle instanceof EntradaDetalleCeye){
				if(solicitado ==null || solicitado < 0.0){
					return "Solicitado Invalido"
				}				
			}
			else if(detalle instanceof EntradaDetalleFarmacia){
				if(precioEntrada == null || precioEntrada < 0.0){
					return "Precio Invalido"
				}
			}
			
			if(cantidad ==null || cantidad < 0.0){
				return "Cantidad Invalida"
			}
			else{								
				def salidasDetalle = salidasDetalle(idEntrada, clave)

				if(salidasDetalle){
					def sumaSurtido = salidasDetalle.sum { it.cantidadSurtida }
					if(cantidad < sumaSurtido)
						return "Cantidad utilizada $sumaSurtido"
				}
			}				
			
			detalle.existencia += (cantidad - detalle.cantidad)
			detalle.cantidad = cantidad						
			
			if(detalle instanceof EntradaDetalleFarmacia){
				detalle.precioEntrada = precioEntrada
				detalle.noLote = params.noLote
				try{
					detalle.fechaCaducidad =  new Date().parse("dd/MM/yyyy",params.fechaCaducidad)
				}
				catch(Exception e){
					detalle.fechaCaducidad = new Date()
					return "Formato fecha caducidad invalido"
				}
			}
			else if(detalle instanceof EntradaDetalleCeye){
				detalle.cantidadSolicitada = solicitado
				detalle.noLote = null
				detalle.fechaCaducidad = null
			}		
		}

		this.actualizarCostoPromedio(detalle,almacen)		
		detalle.save([validate:false])	
		
		return "success"
	}

	@Override
	def borrarDetalle(Long idEntrada, Long clave, String almacen){
		
		def salidaDetalle = salidasDetalle(idEntrada,clave)
		
		if(salidaDetalle.size() != 0){
			return "Clave ya tiene salida"
		}
			
		def entradasDetalle = entityEntradaDetalle.createCriteria().list(){
			entrada { eq('id', idEntrada)}
			articulo {eq("id", clave)}
		}
			
		for(entradaDetalle in entradasDetalle){			
			this.cancelarCostoPromedio(entradaDetalle,almacen)
			entradaDetalle.delete()
		}
		
		return "success"
	}

	@Override
	def consultarDetalle(params){

		//def sortIndex = params.sidx ?: 'id'
		//def sortOrder  = params.sord ?: 'asc'
		//def maxRows = Integer.valueOf(params.rows)
		def currentPage = 1
		//def rowOffset = currentPage == 1 ? 0 : (currentPage - 1) * maxRows
		def idEntrada  = params.long('idPadre')

		log.info("IDENTRADA " + params.idPadre)

		/*def detalleCount = entityEntradaDetalle.createCriteria().list(){
			eq('entrada.id',idEntrada)
		}*/

		//Para la busqueda
		def searchOper = params.searchOper
		def searchString = params.searchString
		def searchField = params.searchField
		def search = params._search		
		
		def criteria = entityEntradaDetalle.createCriteria()
		

		def detalle = criteria.list {
			eq('entrada.id',idEntrada)
			
			if(search == 'true'){
				if(searchOper == 'eq' && searchField == 'cveArt'){
					if(searchString)
						eq('articulo.id',searchString.toLong())
				}
				 
			}
			
			order('renglon', 'asc')
		}
		
		

		def totalRows = detalle.size();
		def numberOfPages = 1

		def results = detalle?.collect {
			
			if(entityEntradaDetalle.name.contains("Ceye")){
				[cell:[it.articulo.id,it.articulo.desArticulo?.trim(),
						it.articulo.unidad?.trim(),
						it.cantidadSolicitada,it.cantidad,it.precioEntrada,
						it.noLote,it.fechaCaducidad?.format('dd/MM/yyyy')], id: it.articulo.id
				]				
			}
			else{
				[cell:[it.articulo.id,it.articulo.desArticulo?.trim(),
					it.articulo.unidad?.trim(),0,it.cantidad,it.precioEntrada,
					it.noLote,it.fechaCaducidad?.format('dd/MM/yyyy')], id: it.articulo.id
				]
			}
			
			
		}

		def jsonData = [rows: results, page: currentPage, records: totalRows, total: numberOfPages]
		return jsonData

	}

	@Override
	def consecutivoRenglon(E entrada){
		utilService.consecutivoRenglon(entityEntradaDetalle,"entrada",entrada)
	}

	@Override
	def checkFolio(Integer folio, String almacen){

		utilService.checkFolio(entityEntrada, folio,almacen)

	}

	@Override
	def consecutivoFolio(String almacen){
		utilService.consecutivoFolio(entityEntrada, almacen)
	}
	
	@Override
	def usuarios(Long idPerfil){
		return utilService.usuarios(idPerfil)
	}

	@Override
	def buscarArticulo(Long id,String almacen=null){
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
	def checkCierre(Date fecha, String almacen){
		utilService.checkCierre(entityCierre, fecha, almacen)
	}
	
	@Override
	def reporte(Long id, String almacen){
		
		def entityEntradaDetalleName = entityEntradaDetalle.name
		
		def whereCeye = ""
		
		if(almacen != 'F'){
			whereCeye = " left join fetch e.area left join fetch e.paciente "
		}
					
		def query =
		"""
			select ed from $entityEntradaDetalleName ed
			left join fetch ed.entrada e 
			left join fetch e.recibio 
			left join fetch e.supervisor
			left join fetch e.usuario
			$whereCeye
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
	
	def disponibilidadArticulo(Long clave, Date fecha,String almacen, E entradaParam = null){
				
		def criteria = entityEntradaDetalle.createCriteria()

		def disponible = criteria.get {

			projections {
				sum("existencia")
			}

			entrada {
				eq("almacen", almacen)
				eq("estado","A")
				le("fecha",fecha)
			}

			articulo {
				eq("id",clave)
			}
			
			if(entradaParam){
				eq("entrada",entradaParam)
			}
			
		}

		if(!disponible)
			return 0
		else
			return disponible
	}
	
	def disponibilidadArticuloAnio(Long clave, Date fecha,String almacen, E entradaParam = null){
		
		def criteria = entityEntradaDetalle.createCriteria()
		
		Calendar fechaCal = Calendar.getInstance();
		fechaCal.setTime(fecha);
 
		int anio = fechaCal.get(Calendar.YEAR);
		
		def disponible = criteria.get {
		
			projections {
				sum("existencia")
			}
		
			entrada {
				eq("almacen", almacen)
				eq("estado","A")				
				sqlRestriction(" year(fecha_entrada)= $anio ")
			}
		
			articulo {
				eq("id",clave)
			}
			
			if(entradaParam){
				eq("entrada",entradaParam)
			}
			
		}
		
		if(!disponible)
			return 0
		else
			return disponible
	}
	
	
	
	def getCostoPromedio(entradaDetalle, String almacen){
		
		double costo = 0.0
			
		def articulo = entradaDetalle.articulo		
		def movimientoProm = utilService.getMovimientoPromedio(articulo,almacen)	
		
		def sumExistencia = disponibilidadArticuloAnio(articulo.id, entradaDetalle.entrada.fecha,almacen)
		
		def denominador = sumExistencia + entradaDetalle.cantidad
		
		if(denominador == 0.0)
			denominador = 1.0
		
		costo = ((sumExistencia * movimientoProm) + (entradaDetalle.cantidad * entradaDetalle.precioEntrada)) / denominador
		
		if(costo < 0.0)
			costo = 0.0
		
		
		utilService.setMovimientoPromedio(articulo, costo, almacen)		
	}
	
	
	def cancelarCostoPromedio(entradaDetalle, String almacen){
		
		double costo = 0.0
		
		def articulo = entradaDetalle.articulo				
		def movimientoProm = utilService.getMovimientoPromedio(articulo,almacen)
		
		def sumExistencia = disponibilidadArticuloAnio(articulo.id, entradaDetalle.entrada.fecha,almacen)
		
		def denominador = (sumExistencia - entradaDetalle.cantidad)
		
		if(denominador == 0.0)
			denominador = 1.0
		
		costo = (( sumExistencia * movimientoProm)-(entradaDetalle.cantidad*entradaDetalle.precioEntrada)) / denominador
		
		if(costo < 0.0)
			costo = 0.0
		
		utilService.setMovimientoPromedio(entradaDetalle.articulo, costo,almacen)	
	}
	
	def actualizarCostoPromedio(entradaDetalle, String almacen){
		
		double costo = 0.0
		
		def articulo = entradaDetalle.articulo
		def movimientoProm = utilService.getMovimientoPromedio(articulo,almacen)
		
		def sumExistencia = disponibilidadArticuloAnio(articulo.id, entradaDetalle.entrada.fecha,almacen)
		def sumExistenciaEntrada = disponibilidadArticuloAnio(articulo.id, entradaDetalle.entrada.fecha,almacen,entradaDetalle.entrada)
		
		def denominador = (sumExistencia + entradaDetalle.cantidad - sumExistenciaEntrada)
		
		if(denominador == 0.0)
			denominador = 1.0
		
		costo = (( (sumExistencia  - sumExistenciaEntrada) * movimientoProm)+(entradaDetalle.cantidad*entradaDetalle.precioEntrada)) / denominador
		
		if(costo < 0.0)
			costo = 0.0
		
		utilService.setMovimientoPromedio(entradaDetalle.articulo, costo,almacen)
		
	}
	
	def cancelarTodoCostoPromedio(E entrad, String almacen){
		def entradasDetalle = entityEntradaDetalle.createCriteria().list(){
			entrada { eq('id', entrad.id)}
			articulo {}
		}
		
		for(entradaDetalle in entradasDetalle){		
			cancelarCostoPromedio(entradaDetalle, almacen)
		}		
		
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
	
	def salidasDetalle(Long idEntrada, Long clave = null){
		
		def criteria = entitySalidaDetalle.createCriteria()
		
		def detalle = criteria.list(){
			entradaDetalle{
				eq('entrada.id', idEntrada)
				
				if(clave)
					eq("articulo.id", clave)
			}
		}
		
		detalle
		
	}
	
	
	

}
