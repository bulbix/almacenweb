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
   
   
   def checkFolio(entity, numero, fecha, Integer folioEntrada){
	   
	   def fechas = fechasAnioActual()
			   
	   def criteria  = entity.createCriteria();
	   
	   def result = criteria.get(){
		   eq(numero,folioEntrada)
		   between(fecha,fechas.fechaInicio,fechas.fechaFin)
		   eq("almacen",'F')
	   }
	    
	   if(result)
		   return true
	   else
		   return false
   }
   
   def consecutivoNumero(entity, numero, fecha){
	   
	   def fechas = fechasAnioActual()
	   
	   def criteria = entity.createCriteria()
	   
	   def maxNumber = criteria.get {
		   
		   projections {
			   max(numero)
		   }
		   
		   between(fecha,fechas.fechaInicio,fechas.fechaFin)
		   
	   }
	   
	   if(!maxNumber)
		   return 1
	   else
		   return maxNumber+1
	   
	}
   
   def consecutivoRenglon(entity, renglon, parent, id){
	   
	   def fechas = fechasAnioActual()
	   
	   def criteria = entity.createCriteria()
	   
	   def maxNumber = criteria.get {
		   
		   projections {
			   max(renglon)
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
   
   
   
}
