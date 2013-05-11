package mx.gob.inr.ceye

import mx.gob.inr.utils.Entrada

class EntradaCeye extends Entrada {

	CatAreaCeye area
	String paqueteq
		
	static hasMany = [entradasDetalle:EntradaDetalleCeye]
	
	static transients = ['folioAlmacen']

	static mapping = {
		id column:'id_entrada'
		
		folio column:'numero_entrada'
		fecha column:'fecha_entrada'
		estado column:'estado_entrada'
		
		recibio column:'recibio'
		supervisor column:'supervisor'
		usuario column:'id_usuario'
		area column:'cve_area'
		id generator:'sequence' ,params:[sequence:'sq_identradaceye']
		table 'entrada_ceye'
		version false
	}
	
    static constraints = {
    }
}
