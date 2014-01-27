package mx.gob.inr.utils

import grails.plugins.springsecurity.SpringSecurityService;

import java.util.Date;
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse

import mx.gob.inr.ceye.ArticuloCeye
import mx.gob.inr.farmacia.ArticuloFarmacia
import mx.gob.inr.ceye.CostoPromedioCeye
import mx.gob.inr.seguridad.*;
import mx.gob.inr.utils.domain.Articulo;
import mx.gob.inr.utils.domain.CatArea
import mx.gob.inr.utils.domain.Cierre
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsHttpSession
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.context.request.RequestContextHolder

class UtilService {
	
	
	def grailsApplication
	
	SpringSecurityService springSecurityService
	
	
	
	def clave(entityArticulo, String tipo){
		def criteria = entityArticulo.createCriteria();
		
		def number = criteria.get {
			projections{
				if(tipo == 'min')
					min("id")
				else if(tipo=="max")
					max("id")
			}
		}
		number
	}
	
	/****
	 * Regresa el primer dia del mes en curso
	 * @param tipo
	 * @return
	 */
	def fechaPrimeroMes(){
		def fecha = new Date()
		
		def fechaPrimero = new Date()
		fechaPrimero.set(month:fecha.getAt(Calendar.MONTH),date:1)
		fechaPrimero
	}
	
	/****
	 * Se obtiene el anio en el archivo de configuracion
	 * @return
	 */
	def anioConfiguracion(){
		
		GrailsWebRequest request = RequestContextHolder.currentRequestAttributes()
		GrailsHttpSession session = request.session
		
		def anio  = null
		 
		switch(session.almacen){
			case "F":
				anio  = grailsApplication.config.almacenWeb.farmacia.anioActual
				break
			case "C":
				anio  = grailsApplication.config.almacenWeb.ceye.anioActual
				break
			case "S":
				anio  = grailsApplication.config.almacenWeb.subceye.anioActual
				break
			case "Q":
				anio  = grailsApplication.config.almacenWeb.ceniaqceye.anioActual
				break
		}
		
		return anio
		
	}
		
   /*****
	*Toma el anio de un archivo de configuracion si no existe toma el anio actual
	* @return
	*/
   def fechasAnioActual(){
	   
	   def anio  = anioConfiguracion()
		
		if(!anio){
			def fecha = new Date()
			anio = fecha.getAt(Calendar.YEAR)
		}
		
		def fecha1 = new Date()
		fecha1.set(month:0,date:1, year:anio)
		
		def fecha2 = new Date()
		fecha2.set(month:11,date:31, year:anio)
		
		[fechaInicio:fecha1,fechaFin:fecha2]
	}
   
   
   def usuarios(Long idPerfil){
	   
	   def perfil = Perfil.get(idPerfil)
	   
	   def usuariosPerfilList = UsuarioPerfil.createCriteria().list(){
			   projections{
			   property("usuario.id")
			}
			   eq("perfil",perfil)
	   }
	   
	   def usuariosList = Usuario.createCriteria().list {
		   'in'("id",usuariosPerfilList)
			order("nombre")
	   }
	   
	   usuariosList
   }
   
   
   boolean checkFolio(entity, Integer folio, String almacen){
	   
	   def fechas = fechasAnioActual()
			   
	   def criteria  = entity.createCriteria();
	   
	   def result = criteria.get(){
		   eq("folio",folio)
		   between("fecha",fechas.fechaInicio,fechas.fechaFin)
		   eq("almacen",almacen)
	   }
		
	   if(result)
		   return true
	   else
		   return false
   }
   
   def consecutivoFolio(entity, String almacen){
	   
	   def fechas = fechasAnioActual()
	   
	   def criteria = entity.createCriteria()
	   
	   def maxNumber = criteria.get {
		   
		   projections {
			   max("folio")
		   }
		   
		   between("fecha",fechas.fechaInicio,fechas.fechaFin)
		   eq("almacen",almacen)
		   
	   }
	   
	   if(!maxNumber)
		   return 1
	   else
		   return maxNumber+1
	   
	}
   
   def consecutivoRenglon(entity, parent, id){
	   
	   def fechas = fechasAnioActual()
	   
	   def criteria = entity.createCriteria()
	   
	   def maxNumber = criteria.get {
		   
		   projections {
			   max("renglon")
		   }
		   
		   eq(parent, id)
	   }
	   
	   if(!maxNumber)
		   return 1
	   else
		   return maxNumber+1
	   
   }
   
   def fechaDesglosada(Date fecha){
	   Calendar cal = Calendar.getInstance();
	   cal.setTime(fecha);
	   
	   [anio:cal.get(GregorianCalendar.YEAR),
		   mes:cal.get(GregorianCalendar.MONTH), dia:cal.get(GregorianCalendar.DAY_OF_WEEK)]
   }
   
   boolean checkCierre(entityCierre, Date fecha, String almacen){
	   
	   def criteria = entityCierre.createCriteria();
	   
	   def fechaDesglosada = fechaDesglosada(fecha)
	   
	   def result = criteria.get {
		   sqlRestriction("month(fecha_cierre) = ($fechaDesglosada.mes + 1)")
		   sqlRestriction("year(fecha_cierre) = $fechaDesglosada.anio")
		   eq("almacen", almacen)
		   maxResults(1)
	   }
	   
	   if(result)
		   return true
	   else
		   return false
   }
   
   def cierreAnterior(entityCierre, Date fechaCierre, String almacen, Articulo articulo = null){
		
		def mFechaCierre = fechaDesglosada(fechaCierre)
		def fechaCierreAnterior = new Date()
		
		Calendar cal = Calendar.getInstance();
		
		if(mFechaCierre.mes == 0){
			cal.set(mFechaCierre.anio - 1 , 11 , 1)//01/12/anio ANterior
		}
		else{
			cal.set(mFechaCierre.anio , mFechaCierre.mes -1 , 1) //01/mes ANterior/anio Cierre
		}
		
		fechaCierreAnterior  = cal.getTime();
		
		def mFechaCierreAnterior = fechaDesglosada(fechaCierreAnterior)
		
		def cierre = entityCierre.createCriteria().get{
			sqlRestriction("month(fecha_cierre) = ($mFechaCierreAnterior.mes + 1)")
			sqlRestriction("year(fecha_cierre) = $mFechaCierreAnterior.anio")
			if(articulo){
				eq("articulo",articulo)
			}
			eq("almacen", almacen)
			maxResults(1)
		}
		
		if(!cierre){
			cierre = entityCierre.newInstance()
			def result = new Date()
			result.set(year:2000,month:0,date:1)
			cierre.fechaCierre = result
			cierre.existencia = 0
			cierre.importe = 0.0
		}
		
		return cierre
	}
   
   /***
	*
	* @return Maxima fecha de cierre del periodo
	*/
   Date maximaFechaCierre(entityCierre, String almacen){
	   
	   def result = new Date()
	   result.set(year:2000,month:0,date:1)
	   
	   def criteria = entityCierre.createCriteria()
	   
	   def max = criteria.get {
		   
		   projections {
			   max("fechaCierre")
		   }
		   
		   eq("almacen", almacen)
	   }
	   
	   if(max)
		   result = max
	   
	   return result
   }
		 
   
   String getAlmacenDescripcion(String almacen){
	   
	   def almacenMap = [F:'FARMACIA',C:'CEYE',S:'SUBCEYE',Q:'CENIAQ CEYE']
	   almacenMap[almacen]
   }
   
   
   def getMovimientoPromedio(Articulo articulo,String almacen){
	   
	   def movimientoProm = 0.0
	   
	   if(articulo instanceof ArticuloFarmacia){
		   movimientoProm = articulo.movimientoProm
	   }
	   else if(articulo instanceof ArticuloCeye){
		   def costoPromedio  = CostoPromedioCeye.createCriteria().get{
			   eq("articulo",articulo)
			   eq("almacen",almacen)
		   }
		   movimientoProm = costoPromedio?.movimientoProm?:0.0
	   }
	   
	   return movimientoProm
   }
   
   def setMovimientoPromedio(Articulo articulo, double costo, String almacen){
	   
	   if(articulo instanceof ArticuloFarmacia){
		   articulo.movimientoProm = costo
		   articulo.save([validate:false])
	   }
	   else if(articulo instanceof ArticuloCeye){
		   def costoPromedio  = CostoPromedioCeye.createCriteria().get{
			   eq("articulo",articulo)
			   eq("almacen",almacen)
		   }
		   
		   if(costoPromedio){
			   costoPromedio.movimientoProm = costo
			   costoPromedio.save([validate:false])
		   }
	   }
   
   }
   
   /**
	* Revisa si es un administrador del modulo o no
	* @param almacen
	* @return
	*/
   def isAdmin(String almacen){
	   
	   def result = false
	   if(almacen == 'F'){
		   result = springSecurityService.authentication.authorities.find {a -> a.authority == 'ROLE_FARMACIA_ADMIN'} != null
	   }
	   else{
		   result = springSecurityService.authentication.authorities.find {a -> a.authority == 'ROLE_CEYE_ADMIN'} != null
	   }
	   
	   result
   }
   
   String getFechaActual(String format){
	   
	   def fecha = new Date()
	   return fecha.format(format)
	   
   }
   
   
   /******
	*
	* Muestra un reporte generado en itext con el arreglo de bytes
	*
	*
	*
	* @param response
	* @param datos
	* @param contentType
	* @param nombre
	* @throws Exception
	*/
   public void mostrarReporte(HttpServletResponse response, InputStream datos,
	   String contentType, String nombre) throws Exception {
		   response.setContentType(contentType);
		   response.setHeader("Content-disposition", "attachment;filename="
				   + nombre);
		   ServletOutputStream os = response.getOutputStream(); //
		   
		   int readBytes=0;
		   int size=0;
		   byte[] buffer=new byte[2048];
		   while(  (readBytes=datos.read(buffer)) >=0){
			   os.write(buffer, 0, readBytes);
			   size+=readBytes;
		   }
		   response.setContentLength(size);
		   os.flush();
		   os.close();
		   response.flushBuffer();
   }
	   
	
	/******.
	 * Busca las areas de ceye por almacen
	 *
	 * @param almacen
	 * @return
	 */
	List<CatArea> areasByAlmacen(String almacen){
		
		if(almacen != 'F'){
			return CatAreaCeye.findAllByAlmacen(almacen, [sort: "id", order: "asc"])
		}
		else{
			return CatAreaFarmacia.findAll([sort: "id", order: "asc"])
		}
	}
	
	List<List<?>> partitionList(List<?> areas, Integer number){
		
		return areas.collate(number)
	}

  
   
}
