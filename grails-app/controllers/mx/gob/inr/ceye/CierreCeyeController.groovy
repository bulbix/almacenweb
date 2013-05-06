package mx.gob.inr.ceye
import javax.annotation.PostConstruct;
import mx.gob.inr.farmacia.CierreFarmacia;
import mx.gob.inr.utils.CierreController;

class CierreCeyeController extends CierreController<CierreCeye> {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	CierreCeyeService cierreCeyeService
	
	public CierreCeyeController(){
		super(CierreCeye, 'C')
	}
	
	@PostConstruct
	public void init(){
		super.cierreService = cierreCeyeService
	}
    
}
