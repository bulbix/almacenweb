package mx.gob.inr.utils.domain

import java.util.Date;

import mx.gob.inr.farmacia.ArticuloFarmacia;
import mx.gob.inr.farmacia.EntradaFarmacia;
import mx.gob.inr.farmacia.EntradaDetalleFarmacia;
import mx.gob.inr.farmacia.SalidaFarmacia;

abstract class SalidaDetalle implements Serializable {
	
	//Salida salida
	Integer renglon
	//Entrada entrada
	Integer renglonEntrada
	//Articulo articulo
	Integer cantidadPedida
	Integer cantidadSurtida
	Date fechaCaducidad
	String noLote
	Double precioUnitario
	String actividad
	String presupuesto
	//EntradaDetalle entradaDetalle
	
	Double importe

}
