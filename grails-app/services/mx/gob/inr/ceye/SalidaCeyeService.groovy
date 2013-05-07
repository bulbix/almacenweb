package mx.gob.inr.ceye

import javax.annotation.PostConstruct;
import mx.gob.inr.farmacia.EntradaFarmaciaService;
import mx.gob.inr.utils.AutoCompleteService;
import mx.gob.inr.utils.SalidaService
import mx.gob.inr.utils.UtilService;


class SalidaCeyeService extends SalidaService<SalidaCeye> {

	UtilService utilService
	EntradaCeyeService entradaCeyeService
	AutoCompleteService autoCompleteService
			
	static transactional = true	
	
	public SalidaCeyeService(){
		super(SalidaCeye,SalidaDetalleCeye, EntradaDetalleCeye, 
			ArticuloCeye,CatAreaCeye, "C")						
	}
	
	@PostConstruct
	public void init(){
		super.utilService = this.utilService
		super.entradaService =  this.entradaCeyeService
		super.autoCompleteService = this.autoCompleteService	
	}
}
