package mx.gob.inr.ceye

import javax.annotation.PostConstruct;
import mx.gob.inr.farmacia.EntradaFarmaciaService;
import mx.gob.inr.utils.SalidaService
import mx.gob.inr.utils.UtilService;


class SalidaCeyeService extends SalidaService<SalidaCeye> {

	UtilService utilService
	EntradaCeyeService entradaCeyeService
			
	static transactional = true	
	
	public SalidaCeyeService(){
		super(SalidaCeye,SalidaDetalleCeye, EntradaDetalleCeye, ArticuloCeye, "C")						
	}
	
	@PostConstruct
	public void init(){
		super.utilService = this.utilService
		super.entradaService =  this.entradaCeyeService	
	}
}
