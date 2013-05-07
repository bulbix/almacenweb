package mx.gob.inr.ceye
import mx.gob.inr.farmacia.SalidaFarmacia;
import mx.gob.inr.utils.SalidaController;

import javax.annotation.PostConstruct;

import mx.gob.inr.farmacia.EntradaFarmaciaService;
import mx.gob.inr.farmacia.SalidaFarmaciaService;

class SalidaCeyeController extends SalidaController<SalidaCeye> {
	EntradaCeyeService entradaCeyeService
	SalidaCeyeService salidaCeyeService
	
	public SalidaCeyeController(){
		super(SalidaCeye,'C')
	}
	
	@PostConstruct
	public void init(){
		super.entradaService = entradaCeyeService
		super.salidaService = salidaCeyeService
	}
}
