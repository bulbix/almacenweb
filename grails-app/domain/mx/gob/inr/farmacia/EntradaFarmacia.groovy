package mx.gob.inr.farmacia

import mx.gob.inr.utils.domain.Entrada;

class EntradaFarmacia extends Entrada {	
	
	String devolucion
	
	static hasMany = [entradasDetalle:EntradaDetalleFarmacia]
	
	static transients = ['folioAlmacen','dueno']

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
		
		usuario updateable: false
		idSalAlma updateable:false
    }    

    static constraints = {		
    }
	
}