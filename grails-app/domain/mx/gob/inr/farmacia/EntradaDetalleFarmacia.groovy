package mx.gob.inr.farmacia

import java.io.Serializable;
import mx.gob.inr.utils.EntradaDetalle

class EntradaDetalleFarmacia extends EntradaDetalle {
	
	
	
	EntradaFarmacia entrada	
	ArticuloFarmacia articulo	
	
	static transients = ['restarExistencia']

    static mapping = {
		
		entrada column:'id_entrada'
		articulo column :'cve_art'
		version false
		table 'entrada_detalle'
		id column:'id_entradadetalle'
		id generator:'sequence' ,params:[sequence:'sq_identradadetalle']
		
		
		//id composite: ['entrada','renglonEntrada']
    }
}