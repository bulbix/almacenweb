package mx.gob.inr.ceye

class CostoPromedioCeye implements Serializable {

	ArticuloCeye articulo
	Double movimientoProm
	String almacen
	
	static mapping = {		
		table 'costopromedio_ceye'
		articulo column:'cve_art'
		id composite: ['articulo','almacen']
		version false
	}
	
	
    static constraints = {
    }
}
