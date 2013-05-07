package mx.gob.inr.farmacia

import java.io.Serializable;
import java.util.Date;
import mx.gob.inr.utils.SalidaDetalle

class SalidaDetalleFarmacia extends SalidaDetalle {
	
	SalidaFarmacia salida
	EntradaFarmacia entrada
	ArticuloFarmacia articulo
	EntradaDetalleFarmacia entradaDetalle	
	
	static constraints = {
				
	}
	
	static mapping = {
		version false
		table 'salida_detalle'
		salida column:'id_salida'
		articulo column :'cve_art'
		//entradaDetalle column:['id_entrada','renglon_entrada']
		entradaDetalle column:'id_entradadetalle'
		entrada column:'id_entrada'		
		id column:'id_salidadetalle'
		id generator:'sequence' ,params:[sequence:'sq_idsalidadetallefarmacia']
	}    
}
