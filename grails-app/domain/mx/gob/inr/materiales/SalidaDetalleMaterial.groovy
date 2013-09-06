package mx.gob.inr.materiales

import java.io.Serializable;
import java.util.Date;

import mx.gob.inr.farmacia.ArticuloFarmacia


class SalidaDetalleMaterial implements Serializable {

	SalidaMaterial salida
	Integer renglon
	//ArticuloMaterial articulo
	Integer cantidadPedida
	Integer cantidadSurtida
	Date fechaCaducidad
	String noLote
	Double precioUnitario
	String almacen
	Integer cveArt
	
	//static hasMany = [articulo:ArticuloMaterial]
	
    static constraints = {
		
    }
	
	static mapping = {		
		version false
		table 'salida_detalle'
		salida column:'id_salida'
		//articulo column : ["cve_art","id_almacen"]
		almacen column:'id_almacen'
		renglon column: 'renglon_salida'
		id composite: ['salida','renglon']
		precioUnitario column:'costo_promedio'
		datasource 'materiales'
	}
	
}
