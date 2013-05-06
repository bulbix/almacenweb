package mx.gob.inr.farmacia

import javax.annotation.PostConstruct;

import grails.converters.JSON
import org.springframework.dao.DataIntegrityViolationException

import mx.gob.inr.farmacia.EntradaFarmaciaService;
import mx.gob.inr.farmacia.EntradaFarmacia;
import mx.gob.inr.materiales.SalidaMaterial;
import mx.gob.inr.utils.EntradaController

class EntradaFarmaciaController extends EntradaController<EntradaFarmacia> {

    
	EntradaFarmaciaService entradaFarmaciaService
	
	
	public EntradaFarmaciaController(){
		super(EntradaFarmacia,CatAreaFarmacia, 'F')
	}
	
	@PostConstruct
	public void init(){
		super.entradaService = entradaFarmaciaService		
	}
	
}
