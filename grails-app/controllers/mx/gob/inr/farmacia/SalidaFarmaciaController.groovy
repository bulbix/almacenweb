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
	
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	EntradaFarmaciaService entradaFarmaciaService
	SalidaFarmaciaService salidaFarmaciaService
	
	public SalidaFarmaciaController(){
		super(SalidaFarmacia,CatAreaFarmacia, 'F')
	}
	
	@PostConstruct
	public void init(){
		super.entradaService = entradaFarmaciaService
		super.salidaService = salidaFarmaciaService
	}
}
