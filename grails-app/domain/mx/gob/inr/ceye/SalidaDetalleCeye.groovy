package mx.gob.inr.ceye

import mx.gob.inr.farmacia.EntradaDetalleFarmacia;
import mx.gob.inr.utils.SalidaDetalle


class SalidaDetalleCeye extends SalidaDetalle {

	SalidaCeye salida
	EntradaCeye entrada
	ArticuloCeye articulo
	EntradaDetalleCeye entradaDetalle
		
	static belongsTo = [salida:SalidaCeye]
	
	static constraints = {
				
	}
	
	static mapping = {
		version false
		table 'salida_detalle_ceye'
		salida column:'id_salida'
		articulo column :'cve_art'
		renglon column: 'renglon_salida'
		entrada column:'id_entrada'
		//entradaDetalle column:['id_entrada','renglon_entrada']
		entradaDetalle column:'id_entradadetalle'
		id column:'id_salidadetalle'
		id generator:'sequence' ,params:[sequence:'sq_idsalidadetalleceye']
	}    
}
