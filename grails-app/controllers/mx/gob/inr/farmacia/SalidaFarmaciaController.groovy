package mx.gob.inr.farmacia

import javax.annotation.PostConstruct;

import grails.converters.JSON
import mx.gob.inr.farmacia.EntradaFarmaciaService;
import mx.gob.inr.farmacia.SalidaFarmaciaService;
import mx.gob.inr.farmacia.SalidaFarmacia;
import mx.gob.inr.utils.SalidaController
import mx.gob.inr.utils.Usuario;
import mx.gob.inr.farmacia.CatAreaFarmacia;
import mx.gob.inr.utils.Paciente;

import org.springframework.dao.DataIntegrityViolationException

class SalidaFarmaciaController extends SalidaController<SalidaFarmacia> {
	
	SalidaFarmaciaService salidaFarmaciaService
	
	public SalidaFarmaciaController(){
		super(SalidaFarmacia, 'F')
	}
	
	@PostConstruct
	public void init(){		
		servicio = salidaFarmaciaService
		super.salidaService = salidaFarmaciaService
	}
}
