package mx.gob.inr.materiales

import java.util.Date;



class SalidaMaterial  {
			
	
	Integer numeroSalida
	Date fechaSalida
	String estadoSalida
	String almacen
	Short cveArea
	
	static hasMany = [salidasDetalle:SalidaDetalleMaterial]
	
    static constraints = {	
		
    }
	
	static mapping = {
		table 'salida'
		id column:'id_salida'
		version false
		datasource 'materiales'
	}
	
	
	
}
