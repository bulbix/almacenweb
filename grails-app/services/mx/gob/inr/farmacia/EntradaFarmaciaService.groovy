package mx.gob.inr.farmacia

import javax.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;
import mx.gob.inr.farmacia.ArticuloFarmacia;
import mx.gob.inr.farmacia.EntradaFarmacia;
import mx.gob.inr.farmacia.EntradaDetalleFarmacia;
import mx.gob.inr.materiales.SalidaDetalleMaterial;
import mx.gob.inr.materiales.SalidaMaterial;
import mx.gob.inr.utils.AutoCompleteService;
import mx.gob.inr.utils.EntradaService
import mx.gob.inr.utils.Usuario;
import mx.gob.inr.utils.UtilService;

class EntradaFarmaciaService extends EntradaService<EntradaFarmacia>  {

	static transactional = true		
	UtilService utilService	
	AutoCompleteService autoCompleteService
	
	final int AREA_FARMACIA = 6220	
	final int PERFIL_FARMACIA = 8
	final int PERFIL_CEYE  = 10
	
	public EntradaFarmaciaService(){
		super(EntradaFarmacia, EntradaDetalleFarmacia,
			ArticuloFarmacia, CatAreaFarmacia, "F")
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
