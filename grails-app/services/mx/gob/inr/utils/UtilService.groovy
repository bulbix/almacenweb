package mx.gob.inr.utils

import java.util.Date;

import mx.gob.inr.utils.Usuario;

class UtilService {	
	
   def fechasAnioActual(){
		
		def fecha1 = new Date()
		fecha1.set(month:0,date:1)
		
		def fecha2 = new Date()
		fecha2.set(month:11,date:31)
		
		[fechaInicio:fecha1,fechaFin:fecha2]
	}
   
   
   def usuarios(idPerfil){
	   def usuariosList = Usuario.createCriteria().list() {
		   perfiles{
			   eq('idPerfil',idPerfil)
		   }
		   order('nombre', 'desc')
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
   
}
