package mx.gob.inr.ceye

class ConvertidoraCeye {
			
	String unidadAlma
	Double cantidadAlma
	String unidadCeye
	Double cantidadCeye
	
	
	static mapping = {
		id generator:'assigned',column:'cve_art'
		version false
		table 'convertidora_ceye'
	}
		
    static constraints = {
    }
}
