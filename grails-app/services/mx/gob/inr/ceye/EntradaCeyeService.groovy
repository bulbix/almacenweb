package mx.gob.inr.ceye
import javax.annotation.PostConstruct;

import mx.gob.inr.utils.AutoCompleteService;
import mx.gob.inr.utils.UtilService;
import mx.gob.inr.utils.EntradaService;

class EntradaCeyeService extends EntradaService<EntradaCeye> {

	static transactional = true
	
	UtilService utilService
	AutoCompleteService autoCompleteService
	
	final int AREA_FARMACIA = 6220
	final int PERFIL_FARMACIA = 8
	final int PERFIL_CEYE  = 10
	
	public EntradaCeyeService(){
		super(EntradaCeye, EntradaDetalleCeye, ArticuloCeye,
			 CatAreaCeye,"C")
	}
	
	@PostConstruct
	public void init(){
		super.utilService = this.utilService
		super.autoCompleteService = this.autoCompleteService
	}
	
	 def consultaMaterial(Integer folioAlmacen) {
		 super.consultaMaterial(folioAlmacen, AREA_FARMACIA)
	 };
    
}
