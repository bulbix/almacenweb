package mx.gob.inr.ceye
import javax.annotation.PostConstruct;
import mx.gob.inr.utils.UtilService;
import mx.gob.inr.utils.EntradaService;

class EntradaCeyeService extends EntradaService<EntradaCeye> {

	static transactional = true
	UtilService utilService
	
	final int AREA_FARMACIA = 6220
	final int PERFIL_FARMACIA = 8
	final int PERFIL_CEYE  = 10
	
	public EntradaCeyeService(){
		super(EntradaCeye, EntradaDetalleCeye, ArticuloCeye)
	}
	
	@PostConstruct
	public void init(){
		super.utilService = this.utilService
	}
	
	def usuariosFarmacia(){
		return utilService.usuarios(PERFIL_CEYE)
	}
	
	
	 def consultaMaterial(Integer folioAlmacen) {
		 super.consultaMaterial(folioAlmacen, AREA_FARMACIA)
	 };
    
}
