package mx.gob.inr.ceye

import mx.gob.inr.utils.EntradaDetalle


class EntradaDetalleCeye extends EntradaDetalle {

    EntradaCeye entrada	
	ArticuloCeye articulo	
	
	static transients = ['restarExistencia']

	static belongsTo = [entrada:EntradaCeye]
	
    static mapping = {
		
		version false
		entrada column:'id_entrada'
		articulo column :'cve_art'		
		table 'entrada_detalle_ceye'
		id column:'id_entradadetalle'
		id generator:'sequence' ,params:[sequence:'sq_identradadetalleceye']
    }
}
