package mx.gob.inr.farmacia
import javax.annotation.PostConstruct;
import mx.gob.inr.utils.UtilService
import mx.gob.inr.utils.services.InventarioService

class InventarioFarmaciaService extends InventarioService {

	static transactional = true
	UtilService utilService
		
	public InventarioFarmaciaService(){
		super(CierreFarmacia, InventarioFarmacia,"F")
	}
	
	@PostConstruct
	public void init(){
		super.utilService = this.utilService
	}	
    
}
