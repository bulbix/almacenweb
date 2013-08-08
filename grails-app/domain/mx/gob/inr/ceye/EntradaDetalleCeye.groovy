package mx.gob.inr.ceye

import mx.gob.inr.utils.domain.EntradaDetalle;


class EntradaDetalleCeye extends EntradaDetalle {

    EntradaCeye entrada	
	ArticuloCeye articulo
	Double cantidadSolicitada
	Integer solicitadoFarmacia
	Integer cantidadFarmacia	
	
	static transients = ['restarExistencia']

	static belongsTo = [entrada:EntradaCeye]
	
    static mapping = {
		
		version false
		entrada column:'id_entrada'
		articulo column :'cve_art'		
		renglon column:'renglon_entrada'
		
		table 'entrada_detalle_ceye'
		id column:'id_entradadetalle'
		id generator:'sequence' ,params:[sequence:'sq_identradadetalleceye']
		
		importe formula: "cantidad * precio_entrada"
    }
	
	static constraints = {
		fechaCaducidad(nullable:true)
		noLote(nullable:true) 
	 }
}
