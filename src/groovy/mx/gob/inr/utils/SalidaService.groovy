package mx.gob.inr.utils

import java.lang.reflect.Constructor
import java.util.Date;

import mx.gob.inr.ceye.SalidaCeye
import mx.gob.inr.farmacia.SalidaFarmacia;

abstract class SalidaService<S extends Salida> implements IOperacionService<S> {
	
	
	public UtilService utilService
	public EntradaService entradaService
	public AutoCompleteService autoCompleteService
	
	protected entitySalida
	protected entitySalidaDetalle
	protected entityEntradaDetalle
	protected entityArticulo
	protected entityArea
	protected entityCierre
	
	protected String almacen
	
	public SalidaService(entitySalida, entitySalidaDetalle,
		 entityEntradaDetalle, entityArticulo, entityArea, entityCierre, almacen){
		this.entitySalida = entitySalida
		this.entitySalidaDetalle = entitySalidaDetalle	
		this.entityEntradaDetalle = entityEntradaDetalle
		this.entityArticulo = entityArticulo
		this.entityArea = entityArea
		this.entityCierre = entityCierre
		this.almacen = almacen		
	}
		 
    @Override
	S setJson(jsonSalida, String ip){		
		
		def salida = entitySalida.newInstance()
		salida.almacen = almacen
		salida.ipTerminal = ip				
		salida.folio = jsonSalida.folio as int
		salida.fecha = new Date().parse("dd/MM/yyyy",jsonSalida.fecha)
		
		salida.jefeServicio = jsonSalida.autorizaauto
		salida.recibio  = jsonSalida.recibeauto
		salida.entrego = Usuario.get(jsonSalida.entrega)
		salida.area = entityArea.get(jsonSalida.cveArea)
		salida.noOrden = null
		salida.usuario = Usuario.get(6558)
		salida.paciente = Paciente.get(jsonSalida.idPaciente)
		
		if(salida instanceof SalidaFarmacia){			
		}
		else if(salida instanceof SalidaCeye){
			salida.diagnostico =  Cie09.get(jsonSalida.idProcedimiento)
			salida.nosala = jsonSalida.nosala?jsonSalida.nosala as short:null
			salida.paqueteq = jsonSalida.paqueteq
		}
		
		
		return salida
	}
	
	@Override
	S guardar(S salida){				
		salida.save([validate:false,flush:true])
		return salida		
	}
	
	@Override
	def guardarDetalle(jsonDetalle, S salida, Integer renglon ){
		
		Long clave = jsonDetalle.cveArt as long 
		Double solicitado = jsonDetalle.solicitado as double
		Double surtido = jsonDetalle.surtido as double
		
		def entradas = cargarEntradasDeSalida(clave, salida.fecha, almacen, surtido)
		
		entradas.each(){
			
			def articulo = entityArticulo.get(jsonDetalle.cveArt)			
			def salidaDetalle = entitySalidaDetalle.newInstance()
			
			salidaDetalle.salida = salida			
			
			if(!renglon)
				salidaDetalle.renglon = consecutivoRenglon(salida)
			else
				salidaDetalle.renglon = renglon
			
			salidaDetalle.entrada = it.entrada
			salidaDetalle.renglonEntrada = it.renglon
			salidaDetalle.articulo = articulo
			salidaDetalle.cantidadPedida = solicitado
			salidaDetalle.cantidadSurtida = it.restarExistencia
			salidaDetalle.fechaCaducidad = it.fechaCaducidad
			salidaDetalle.noLote = it.noLote
			salidaDetalle.precioUnitario = articulo.movimientoProm
			salidaDetalle.presupuesto = null
			salidaDetalle.actividad = null
			salidaDetalle.entradaDetalle = entityEntradaDetalle.get (it.id)
			
			salidaDetalle.save([validate:false,flush:true])
			
			it.existencia -=  it.restarExistencia
			it.save([validate:false,flush:true])
		}
		
		
	}
	
	@Override
	def guardarTodo(S salida, jsonArrayDetalle){

		salida = guardar(salida)

		Integer renglon = 1

		jsonArrayDetalle.each() {
			guardarDetalle(it, salida, renglon++)
		}
		
	}
	
	@Override
	def String actualizar(S salida, Long idSalidaUpdate){		
	
	 def salidaUpdate = entitySalida.get(idSalidaUpdate);
	 
	 def mensaje = "Salida actualizada"
	 
	 if(salida.folio != salidaUpdate.folio ){
		 if (checkFolio(salida.folio)){
			 mensaje = "Folio no actualizable, ya existe";
			 salida.folio = salidaUpdate.folio
		 }
	 }
	 
	 /**Si cambio la fecha de salidahay que regresar existencias y vlver a insertar */
	 if(salidaUpdate.fecha.compareTo(salida.fecha) != 0){
		 
	 }
	 
	 salidaUpdate.properties = salida.properties;
	 salidaUpdate.save([validate:false,flush:true])
	 
	 return "Salida Actualizada";
		
	}
	
	@Override
	def consultar(Long idSalida){
		
	}	
	
	@Override
	def cancelar(Long idSalida){
		
		def salida =  entitySalida.get(idSalida);
		regresarExistencias(salida)

		salida.estado = 'C'
		salida.save([validate:false,flush:true])
	}
	
	@Override
	def listar(params){
		
		def fechas = utilService.fechasAnioActual()
				
		def salidaList = entitySalida.createCriteria().list(params){
			
			between("fecha",fechas.fechaInicio,fechas.fechaFin)			
			order("folio","desc")
		}
		
		def salidaTotal = entitySalida.createCriteria().get{
			projections{
				count()
			}			
			between("fecha",fechas.fechaInicio,fechas.fechaFin)
		}
		
		[lista:salidaList, total:salidaTotal]
		
	}
	
	@Override
	def actualizarDetalle(Long idSalida,params){	
		
		def clave  = params.long('id')
		def solicitado = params.double('solicitado')
		def surtido =params.double('surtido')
		
		def detalle = entitySalidaDetalle.createCriteria().list(){
			eq('salida.id', idSalida)
			eq("articulo.id", clave)			
		}
		
		def sumaSurtido = detalle.sum { it.cantidadSurtida }
		
		def salidaUpdate = entitySalida.get(idSalida)
		
		if(solicitado == null || solicitado < 0.0){
			return "Solicitado Invalido"
		}
		
		if(surtido ==null || surtido < 0.0){
			return "Surtido Invalido"
		}	
		
		
		def disponible = disponibilidadArticulo(clave, salidaUpdate.fecha, almacen) + 
		(sumaSurtido - surtido)
		
		if(disponible < 0){
			return "Cantidad Disponible $disponible"
		}
		
		
		borrarDetalle(idSalida, clave)		
		def jsonDetalle=[cveArt:clave, solicitado:solicitado,surtido:surtido]		
		guardarDetalle(jsonDetalle,salidaUpdate,null)
		return "success"
		
	}
	
	@Override
	def borrarDetalle(Long idSalida, Long clave){
		
		def detalle = entitySalidaDetalle.createCriteria().list(){
			eq('salida.id', idSalida)
			eq("articulo.id", clave)
		}
		
		detalle.each(){
			def entradaDetalle  = it.entradaDetalle
			
			if(entradaDetalle){
				entradaDetalle.existencia += it.cantidadSurtida
				entradaDetalle.save([validate:false,flush:true])
			}
			
			it.delete([flush:true])
		}
		
		return "success"
		
	}
	
	@Override
	def consultarDetalle(params){
		def sortIndex = params.sidx ?: 'id'
		def sortOrder  = params.sord ?: 'asc'
		def maxRows = Integer.valueOf(params.rows)
		def currentPage = Integer.valueOf(params.page) ?: 1
		def rowOffset = currentPage == 1 ? 0 : (currentPage - 1) * maxRows
		def idSalida  = Long.parseLong(params.idPadre)
		
		log.info("IDSALIDA " + params.idPadre)
		
		def entitySalidaDetalleName = entitySalidaDetalle.name
		
		def query =
		"""\
			select sd.articulo.id, sd.articulo.desArticulo, sd.articulo.unidad, sd.articulo.movimientoProm,
			sd.cantidadPedida,sum(sd.cantidadSurtida),sd.salida.fecha
			from $entitySalidaDetalleName sd 			
			where sd.salida.id = $idSalida and sd.salida.almacen = '$almacen' 
			group by sd.articulo.id, sd.articulo.desArticulo, sd.articulo.unidad, sd.articulo.movimientoProm,
			sd.cantidadPedida,sd.salida.fecha 
			order by sd.articulo.id

		"""
		
		def detalleCount = entitySalidaDetalle.executeQuery(query,[])
				
		def detalle = entitySalidaDetalle.executeQuery(query,[],[max: maxRows, offset: rowOffset])
		
		def totalRows = detalleCount.size();
		def numberOfPages = Math.ceil(totalRows / maxRows)
			
		def results = detalle?.collect {
			[
				 cell:[it[0], it[1]?.trim(),
					 it[2]?.trim(), it[3],
					  disponibilidadArticulo(it[0], it[6],almacen),
					  it[4],it[5]], id: it[0]
			]
		}
		
		def jsonData = [rows: results, page: currentPage, records: totalRows, total: numberOfPages]
		
		return jsonData
		
	}
			
	private def regresarExistencias(S salida){
		
		def salidasDetalle = entitySalidaDetalle.createCriteria().list(){
			eq("salida", salida)
		}
		
		salidasDetalle.each(){
			def entradaDetalle  = it.entradaDetalle
			
			if(entradaDetalle){
				entradaDetalle.existencia += it.cantidadSurtida
				entradaDetalle.save([validate:false,flush:true])
			}
		}
		
	}
	
	private def cargarEntradasDeSalida(Long clave, Date fecha, String almacen, Double surtido){
		
		def entradas = entradaService.entradasDetalle(clave, fecha, almacen)
				
		log.info("Num entradas " + entradas.size())
		
		def entradasAfectadas = afectarEntradas(entradas, surtido);
		
		log.info("Num entradas2 " + entradasAfectadas.size())
		
		if(entradasAfectadas.size() == 0){
			
			//def entradaDetalle = new entityEntradaDetalle();	
			def entradaDetalle = entityEntradaDetalle.newInstance();
			
			entradaDetalle.entrada = null
			entradaDetalle.articulo = entityArticulo.get(clave)
			entradaDetalle.cantidad = 0.0
			entradaDetalle.existencia = 0.0
			entradaDetalle.precioEntrada = 0.0
			entradaDetalle.fechaCaducidad = null
			entradaDetalle.noLote = null
			entradaDetalle.restarExistencia = 0.0		

			
			entradasAfectadas.add(entradaDetalle);
		}
		
		entradasAfectadas
	}
		
	private def afectarEntradas(entradas, surtido){
		def despachado = 0.0;
		
		def result = [];
		
		if(surtido == 0){
			return result
		}
		
		for(entradaDetalle in entradas){
			despachado = entradaDetalle.existencia - surtido;
			
			log.info("Despachado " + despachado)
			
			if(despachado < 0){
				entradaDetalle.restarExistencia = entradaDetalle.existencia
				result.add(entradaDetalle)
				surtido = Math.abs(despachado)
			}
			else if(despachado >=0){
				entradaDetalle.restarExistencia = surtido
				result.add(entradaDetalle)
				break
			}
			
		}
		
		return result
	}
	
	@Override
	def consecutivoRenglon(S salida){
		utilService.consecutivoRenglon(entitySalidaDetalle,"salida",salida)
	}
	
	@Override
	def checkFolio(Integer folio){		
		utilService.checkFolio(entitySalida, folio,almacen)		
	}
	
	@Override
	def consecutivoFolio(){
		utilService.consecutivoFolio(entitySalida,almacen)
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
	
	def listarRecibe(String term){
		autoCompleteService.listarNombre(entitySalida, "recibio", term)
	}
	
	
	def listarAutoriza(String term){
		autoCompleteService.listarNombre(entitySalida, "jefeServicio", term)
	}
	
	def listarProcedimiento(String term){
		autoCompleteService.listarProcedimiento(term)
	}
	
	
	def disponibilidadArticulo(Long clave, Date fecha, String almacen){
		
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
		}

		if(!disponible)
			return 0
		else
			return disponible
	}
	
}
