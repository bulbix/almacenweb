package mx.gob.inr.materiales

import java.util.Date;



class SalidaMaterial  {
			
	
	Integer folio
	Date fecha
	String estado
	String almacen
	Short cveArea
	
	static hasMany = [salidasDetalle:SalidaDetalleMaterial]
	
    static constraints = {	
		
    }
	
	static mapping = {
		table 'salida'
		id column:'id_salida'
		
		folio column:'numero_salida'
		fecha column:'fecha_salida'
		estado column:'estado_salida'
		
		version false
		datasource 'materiales'
	}
	
	
	
}
