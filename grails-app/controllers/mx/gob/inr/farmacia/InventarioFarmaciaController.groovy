package mx.gob.inr.farmacia

import javax.annotation.PostConstruct;

import mx.gob.inr.utils.InventarioController

class InventarioFarmaciaController extends InventarioController {

	InventarioFarmaciaService inventarioFarmaciaService
	
	
	public InventarioFarmaciaController(){
		super(CierreFarmacia,"F")
	}
	
	@PostConstruct
	public void init(){
		inventarioService = inventarioFarmaciaService
	}
   
}
