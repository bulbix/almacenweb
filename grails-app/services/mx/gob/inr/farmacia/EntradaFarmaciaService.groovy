package mx.gob.inr.farmacia

import grails.plugins.springsecurity.SpringSecurityService;

import javax.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;
import mx.gob.inr.farmacia.ArticuloFarmacia;
import mx.gob.inr.farmacia.EntradaFarmacia;
import mx.gob.inr.farmacia.EntradaDetalleFarmacia;
import mx.gob.inr.materiales.*;
import mx.gob.inr.utils.AutoCompleteService;
import mx.gob.inr.utils.UtilService;
import mx.gob.inr.utils.services.EntradaService;

class EntradaFarmaciaService extends EntradaService<EntradaFarmacia>  {

	static transactional = true		
	UtilService utilService	
	AutoCompleteService autoCompleteService
	
	public EntradaFarmaciaService(){
		super(EntradaFarmacia, EntradaDetalleFarmacia,SalidaDetalleFarmacia, 
			ArticuloFarmacia, CatAreaFarmacia,CierreFarmacia)
	}
	
	@PostConstruct
	public void init(){
		super.utilService = this.utilService
		super.autoCompleteService = this.autoCompleteService				
	}
	
	def checkFolioSalAlma(Integer folioSalAlma){
		
		def fechas = utilService.fechasAnioActual()

		def criteriaSalida  = SalidaMaterial.createCriteria();

		def resultSalida = criteriaSalida.get(){
			eq("folio",folioSalAlma)
			between("fecha",fechas.fechaInicio,fechas.fechaFin)
			eq("cveArea",AREA_FARMACIA)
			eq("almacen","F")
		}
		
		def result = entityEntrada.createCriteria().get {
			eq("idSalAlma",resultSalida?.id as Integer)
		}

		if(result)
			return true
		else
			return false
	}
	
	
	
	def consultarDetalleMaterial (Integer folioAlmacen, Short cveArea){
		
		def query = """\
			select sd from SalidaDetalleMaterial sd join fetch sd.id.salida s 
			where s.folio = :folio and s.fecha between :fecha1 and :fecha2 and s.almacen = 'F' 
			and s.cveArea = :area and s.estado <> 'C' order by sd.cveArt
		"""
		
		def fechas = utilService.fechasAnioActual()
		
		def detalle  = SalidaDetalleMaterial.executeQuery(query,
			[folio:folioAlmacen,fecha1:fechas.fechaInicio,fecha2:fechas.fechaFin, area:cveArea])
		
		def idSalAlma = null
		
		if(detalle.size() > 0)
			idSalAlma = detalle[0].salida.id
			
		//log.info("ID salida almacen "  + detalle[0].salida.id);
		
		
		def results = detalle?.collect {
			
			def articulo = ArticuloMaterial.findWhere([cveArt:it.cveArt,almacen:it.almacen])
			
			[
				cveArt:it.cveArt,desArticulo:articulo.desArticulo?.trim(),unidad:articulo.unidad?.trim(),
				cantidad:it.cantidadSurtida,precioEntrada:it.precioUnitario,noLote:it.noLote,
				fechaCaducidad:it.fechaCaducidad?.format('dd/MM/yyyy')
			]
		}
		
		return [rows:results,idSalAlma:idSalAlma]
	}
	
}
