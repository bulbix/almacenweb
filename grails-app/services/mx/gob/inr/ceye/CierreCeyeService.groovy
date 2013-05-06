package mx.gob.inr.ceye
import javax.annotation.PostConstruct;

import mx.gob.inr.farmacia.ArticuloFarmacia;
import mx.gob.inr.farmacia.CierreFarmacia;
import mx.gob.inr.utils.CierreService;
import mx.gob.inr.utils.UtilService;



class CierreCeyeService extends CierreService<CierreCeye,ArticuloCeye> {

    UtilService utilService
	static transactional = true
	
	
	public CierreCeyeService(){
		super(EntradaCeye,SalidaCeye,SalidaDetalleCeye,ArticuloCeye,CierreCeye,"C")
	}
	
	@PostConstruct
	public void init(){
		super.utilService = this.utilService
	}
}
