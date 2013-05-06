package mx.gob.inr.ceye

import javax.annotation.PostConstruct;
import mx.gob.inr.farmacia.EntradaFarmaciaService;

import mx.gob.inr.utils.EntradaController;

class EntradaCeyeController extends EntradaController<EntradaCeye> {

	EntradaCeyeService entradaCeyeService	
	
	public EntradaCeyeController(){
		super(EntradaCeye,CatAreaCeye, 'C')
	}
	
	@PostConstruct
	public void init(){
		super.entradaService = entradaCeyeService
	}
  
}
