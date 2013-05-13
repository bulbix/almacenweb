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
	
	public EntradaFarmaciaService(){
		super(EntradaFarmacia, EntradaDetalleFarmacia,SalidaDetalleFarmacia,
			ArticuloFarmacia, CatAreaFarmacia,CierreFarmacia, "F")
	}
	
	@PostConstruct
	public void init(){
		super.utilService = this.utilService
		super.autoCompleteService = this.autoCompleteService		
	}
	
	
	
	def consultarDetalleMaterial (Integer folioAlmacen, Short cveArea){
		
		def query = """\
			select sd from SalidaDetalleMaterial sd join fetch sd.id.salida s 
			where s.folio = :folio and s.fecha between :fecha1 and :fecha2 and s.almacen = 'F' 
			and s.cveArea = :area and s.estado <> 'C' order by sd.articulo
		"""
		
		def fechas = utilService.fechasAnioActual()
		
		def detalle  = SalidaDetalleMaterial.executeQuery(query,
			[folio:folioAlmacen,fecha1:fechas.fechaInicio,fecha2:fechas.fechaFin, area:cveArea])
		
		def idSalAlma = null
		
		if(detalle.size() > 0)
			idSalAlma = detalle[0].salida.id
			
		//log.info("ID salida almacen "  + detalle[0].salida.id);
		
		
		def results = detalle?.collect {
			[
				cveArt:it.articulo.id,desArticulo:it.articulo.desArticulo?.trim(),unidad:it.articulo.unidad?.trim(),
				cantidad:it.cantidadSurtida,precioEntrada:it.precioUnitario,noLote:it.noLote,
				fechaCaducidad:it.fechaCaducidad?.format('dd/MM/yyyy')
			]
		}
		
		return [rows:results,idSalAlma:idSalAlma]
	}
	
}
