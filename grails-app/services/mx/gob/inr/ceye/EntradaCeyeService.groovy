	package mx.gob.inr.ceye
import grails.plugins.springsecurity.SpringSecurityService;

import javax.annotation.PostConstruct;

import mx.gob.inr.farmacia.ArticuloFarmacia;
import mx.gob.inr.farmacia.SalidaDetalleFarmacia;
import mx.gob.inr.utils.AutoCompleteService;
import mx.gob.inr.utils.UtilService;
import mx.gob.inr.utils.services.EntradaService;
import mx.gob.inr.materiales.*;

class EntradaCeyeService extends EntradaService<EntradaCeye> {

	static transactional = true
	
	UtilService utilService
	AutoCompleteService autoCompleteService	
	
	final int AREA_FARMACIA = 6220
	final int PERFIL_FARMACIA = 8
	final int PERFIL_CEYE  = 10
	
	public EntradaCeyeService(){
		super(EntradaCeye, EntradaDetalleCeye,SalidaDetalleCeye, ArticuloCeye, CatAreaCeye,CierreCeye)
	}
	
	@PostConstruct
	public void init(){
		super.utilService = this.utilService
		super.autoCompleteService = this.autoCompleteService		
	}
	
	def convertidora(Long cveArt,Integer cantidad){	
				
		def convertidora = ConvertidoraCeye.get(cveArt);
		
		Integer cantidadConvertida = cantidad
		Double precioConvertido  = precioAlmacen(cveArt)
		Double cociente = 1.00
		String ualma="PIEZA", uceye="PIEZA"
		Double calma=1.00, cceye = 1.00
		
		if(convertidora){
			
			ualma = convertidora.unidadAlma
			calma = convertidora.cantidadAlma
			uceye  = convertidora.unidadCeye
			cceye = convertidora.cantidadCeye
			
			cociente = convertidora.cantidadAlma / convertidora.cantidadCeye
			cantidadConvertida = cantidadConvertida * cociente;
			precioConvertido = precioConvertido / cociente
						
		}
		
		def precioFormat = String.format(new Locale("es","MX"), '%1.4f',precioConvertido)
		[ualma:ualma, calma:calma, uceye:uceye,cceye:cceye,cantidad:cantidadConvertida, precio:precioFormat,
			 cociente:cociente, precioRaw:precioConvertido]
	}
	
	
	def precioAlmacen(Long cveArt){
		
		def costoPromedioMaterial = CostoPromedioMaterial.findWhere(cveArt:cveArt.intValue(),almacen:"F")
		def precio = 0.0
		
		if(costoPromedioMaterial){
			precio = costoPromedioMaterial.costoPromedio			
		}
			
		return precio		
	}
	
	def consultarPaquete(String tipo){
		
		def detalle = PaqueteQuirurgicoCeye.createCriteria().list(){
			eq("tipo", tipo)
		}
		
		def results = detalle?.collect {
			
			def convertido = convertidora(it.articulo.id, it.cantidad)
								
			[cveArt:it.articulo.id,desArticulo:it.articulo.desArticulo?.trim(),unidad:it.articulo.unidad?.trim(),
			solicitado:it.cantidad,cantidad:it.cantidad,precioEntrada:convertido.precioRaw,noLote:null,fechaCaducidad:null]				
		}
		
		results		
	}
	
	/****
	 * Autocompletar los nombres del usuario recibe
	 * @param term
	 * @return
	 */
	def listarRecibe(String term){
		autoCompleteService.listarNombre(EntradaCeye, "usuarioRecibe", term)
	}
	
	/***
	 * Autocompletar los nombres del usuario solicita
	 * @param term
	 * @return
	 */
	def listarSolicita(String term){
		autoCompleteService.listarNombre(EntradaCeye, "usuarioSolicita", term)
	}
	
    
}
