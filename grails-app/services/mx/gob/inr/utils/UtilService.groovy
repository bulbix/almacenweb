package mx.gob.inr.utils

import java.util.Date;
import mx.gob.inr.ceye.ArticuloCeye
import mx.gob.inr.farmacia.ArticuloFarmacia
import mx.gob.inr.ceye.CostoPromedioCeye
import mx.gob.inr.seguridad.*;
import mx.gob.inr.utils.domain.Articulo;

class UtilService {	
	
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
		
   def fechasAnioActual(){
		
		def fecha1 = new Date()
		fecha1.set(month:0,date:1)
		
		def fecha2 = new Date()
		fecha2.set(month:11,date:31)
		
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
   
   /****
	* Obtiene la fecha de cierre de acuerdo ala fechaInicial
	*
	* @param fecha
	* @return
	*/
   def obtenerFechaCierre(Date fecha) {

	   Calendar fechaCal = Calendar.getInstance();
	   fechaCal.setTime(fecha);

	   int anio = fechaCal.get(Calendar.YEAR);
	   int mes = fechaCal.get(Calendar.MONTH);

	   if (mes == 0) {// mes en 11 y anio menos uno
		   anio = anio - 1;
		   mes = 11;
	   } else {// restamos un mes
		   mes -= 1;
	   }

	   Date fechaCierre;

	   Calendar calFin = Calendar.getInstance();
	   calFin.set(anio, mes, 1);
	   calFin.set(anio, mes, calFin.getActualMaximum(Calendar.DAY_OF_MONTH));
	   fechaCierre = calFin.getTime();
	   return fechaCierre;
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
		   movimientoProm = costoPromedio.movimientoProm
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
		   costoPromedio.movimientoProm = costo
		   costoPromedio.save([validate:false])
	   }
	   
   }
   
}
