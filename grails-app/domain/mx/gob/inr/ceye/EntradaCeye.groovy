package mx.gob.inr.ceye

import mx.gob.inr.utils.Paciente
import mx.gob.inr.utils.domain.Entrada;

class EntradaCeye extends Entrada {

	static auditable = true
	
	CatAreaCeye area
	String paqueteq
	String tipoVale
	Paciente paciente
		
	static hasMany = [entradasDetalle:EntradaDetalleCeye]
	
	static transients = ['folioAlmacen','dueno']

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
		
		usuario updateable: false
		paqueteq updateable:false
		fechaCaptura updateable:false
		
		paciente column:'id_paciente'
	}
	
    static constraints = {
    }
}
