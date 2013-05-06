package mx.gob.inr.farmacia

import javax.annotation.PostConstruct;
import mx.gob.inr.utils.CierreController
import org.springframework.dao.DataIntegrityViolationException

class CierreFarmaciaController extends CierreController<CierreFarmacia> {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	CierreFarmaciaService cierreFarmaciaService
	
	public CierreFarmaciaController(){
		super(CierreFarmacia, 'F')
	}
	
	@PostConstruct
	public void init(){
		super.cierreService = cierreFarmaciaService
	}

 
}
