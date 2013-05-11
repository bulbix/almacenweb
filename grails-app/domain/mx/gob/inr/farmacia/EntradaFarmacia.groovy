package mx.gob.inr.farmacia

import mx.gob.inr.utils.Entrada
import mx.gob.inr.utils.Usuario;

class EntradaFarmacia extends Entrada {	
	
	String devolucion
	
	static hasMany = [entradasDetalle:EntradaDetalleFarmacia]
	
	static transients = ['folioAlmacen']

	static mapping = {
        id column:'id_entrada'
		
		folio column:'numero_entrada'
		fecha column:'fecha_entrada'
		estado column:'estado_entrada'
		
		recibio column:'recibio'
		supervisor column:'supervisor'		
		usuario column:'id_usuario' 
        id generator:'sequence' ,params:[sequence:'sq_identradafarmacia']
        table 'entrada'        
        version false
    }    

    static constraints = {		
    }
	
}