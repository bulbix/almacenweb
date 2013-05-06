package mx.gob.inr.ceye

class PaqueteQuirurgicoCeye implements Serializable {

	ArticuloCeye articulo
	String tipo
	Integer cantidad
	
	static mapping = {
		version false
		table 'paquetequirurgico_ceye'
		articulo column:'cve_art'
		id composite: ['articulo','tipo']
	}
	
	
    static constraints = {
    }
}
