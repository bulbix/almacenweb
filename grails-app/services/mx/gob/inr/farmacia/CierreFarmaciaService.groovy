package mx.gob.inr.farmacia

import javax.annotation.PostConstruct;

import mx.gob.inr.utils.CierreService
import mx.gob.inr.utils.ConcentradoraCierre
import mx.gob.inr.utils.UtilService

class CierreFarmaciaService extends CierreService<CierreFarmacia,ArticuloFarmacia> {
	
	UtilService utilService
	static transactional = true
	
	
	public CierreFarmaciaService(){
		super(EntradaFarmacia,SalidaFarmacia,SalidaDetalleFarmacia,ArticuloFarmacia,CierreFarmacia,"F")
	}
	
	@PostConstruct
	public void init(){
		super.utilService = this.utilService
	}
	
	
}
