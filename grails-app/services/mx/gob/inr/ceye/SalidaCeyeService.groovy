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
			ArticuloCeye,CatAreaCeye,CierreCeye, "C")						
	}
	
	@PostConstruct
	public void init(){
		super.utilService = this.utilService
		super.entradaService =  this.entradaCeyeService
		super.autoCompleteService = this.autoCompleteService	
	}
	
	def consultarPaquete(String tipo, Date fecha){
		
		def detalle = PaqueteQuirurgicoCeye.createCriteria().list(){
			eq("tipo", tipo)
		}
		
		def results = detalle?.collect {		
			 
			//def convertido = convertidora(it.articulo.id, it.cantidad)
			//def precioAlmacen = precioAlmacen(it.articulo.id)
			def disponible = disponibilidadArticulo(it.articulo.id, fecha, almacen)
					
			[cveArt:it.articulo.id,desArticulo:it.articulo.desArticulo?.trim(),unidad:it.articulo.unidad?.trim(),
			costo:it.articulo.movimientoProm,disponible:disponible,solicitado:it.cantidad,surtido:it.cantidad]				
		}
		
		results
		
	}
	
}
