package mx.gob.inr.materiales

import java.io.Serializable;
import java.util.Date;

import mx.gob.inr.farmacia.ArticuloFarmacia


class SalidaDetalleMaterial implements Serializable {

	SalidaMaterial salida
	Integer renglon
	ArticuloMaterial articulo
	Integer cantidadPedida
	Integer cantidadSurtida
	Date fechaCaducidad
	String noLote
	Double precioUnitario
	
	
    static constraints = {
		
    }
	
	static mapping = {		
		version false
		table 'salida_detalle'
		salida column:'id_salida'
		articulo column :'cve_art'
		renglon column: 'renglon_salida'
		id composite: ['salida','renglon']
		datasource 'materiales'
	}
	
}
