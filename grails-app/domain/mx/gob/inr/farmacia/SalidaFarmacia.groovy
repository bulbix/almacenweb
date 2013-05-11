package mx.gob.inr.farmacia

import java.util.Date;

import mx.gob.inr.utils.Paciente;
import mx.gob.inr.utils.Salida
import mx.gob.inr.utils.Usuario;

class SalidaFarmacia extends Salida  {	
	
	CatAreaFarmacia area
	
	static hasMany = [salidasDetalle:SalidaDetalleFarmacia]
	
	static constraints = {
	
	}

	static mapping = {
		table 'salida'
		id column:'id_salida'
		
		folio column:'numero_salida'
		fecha column:'fecha_salida'
		estado column:'estado_salida'
				
		entrego column:'entrego'		
		usuario column:'id_usuario'
		paciente column:'id_paciente'
		area column:'cve_area'
		id generator:'sequence' ,params:[sequence:'sq_idsalidafarmacia']
		version false
	}
	
	
}
