package mx.gob.inr.ceye

import java.io.Serializable;

class PaqueteTipoQuirurgicoCeye implements Serializable {

	String tipo
	String descripcion
	
	static mapping = {
		id composite: ['tipo']				
		table 'paquetetipoquirurgico_ceye'
		version false
	}
	
    static constraints = {
    }
}
