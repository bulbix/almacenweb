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
		entrego column:'entrego'		
		usuario column:'id_usuario'
		paciente column:'id_paciente'
		area column:'cve_area'
		id generator:'sequence' ,params:[sequence:'sq_idsalida']
		version false
	}
	
	
}
