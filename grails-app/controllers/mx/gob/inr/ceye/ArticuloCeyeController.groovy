package mx.gob.inr.ceye

import mx.gob.inr.materiales.ArticuloMaterial;
import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured;

@Secured(['ROLE_CEYE'])
class ArticuloCeyeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	def filterPaneService

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [articuloCeyeInstanceList: ArticuloCeye.list(params),
		 articuloCeyeInstanceTotal: ArticuloCeye.count()]
    }

    def create(Integer id) {		
		
		def articuloCeyeInstance = new ArticuloCeye();
		def sololectura = false		
		
		if(id){		
			articuloCeyeInstance = ArticuloCeye.get(id)
			def articuloAlmacen = ArticuloMaterial.get(id)		
			def convertidora = ConvertidoraCeye.get(id);
			articuloCeyeInstance.descripcionAlmacen = articuloAlmacen.desArticulo
			articuloCeyeInstance.unidadAlmacen = articuloAlmacen.unidad
			articuloCeyeInstance.cantidadAlmacen = convertidora?.cantidadAlma?:1.0
			articuloCeyeInstance.cantidadCeye = convertidora?.cantidadCeye?:1.0
			sololectura = true
		}
						
        [articuloCeyeInstance: articuloCeyeInstance, sololectura:sololectura]
    }
	
	def save(){
		
		def articuloCeye = ArticuloCeye.get(params.clave)
		def articuloAlmacen = ArticuloMaterial.get(params.clave)		
		
		if(!articuloCeye){
			articuloCeye = new ArticuloCeye()
			articuloCeye.properties = articuloAlmacen.properties		
			articuloCeye.id = params.clave as long
			articuloCeye.desArticulo = params.descripcionCeye
			articuloCeye.unidad = params.unidadCeye
			
			def convertidoraCeye = new ConvertidoraCeye(unidadAlma:articuloAlmacen.unidad,
														cantidadAlma:params.cantidadAlmacen as float,
														unidadCeye:params.unidadCeye,
														cantidadCeye:params.cantidadCeye as float);
													
			convertidoraCeye.id = params.clave as long
			
			articuloCeye.save([validate:false])
			convertidoraCeye.save([validate:false])
		}
		else{
			articuloCeye.desArticulo = params.descripcionCeye
			articuloCeye.unidad = params.unidadCeye
			articuloCeye.save([validate:false])			
		}
		
		flash.message = "Articulo registrado existosamente"
		redirect(action: "index")
		
		
	}	
	
	def load(){
		
		def articuloCeyeInstance = new ArticuloCeye();
				
		if(params.clave){
			articuloCeyeInstance = ArticuloCeye.get(params.clave)
			
			if(articuloCeyeInstance){
				flash.message = "La clave ya existe, modifiquela si es el caso"
				redirect(action: "create", id: params.clave)
			}
			else{
				
				def articuloAlmacen = ArticuloMaterial.get(params.clave)
				
				if(!articuloAlmacen){
					flash.message = "No existe la clave en el almacen"
					redirect(action: "create")
				}
				else{
					articuloCeyeInstance = new ArticuloCeye()
					articuloCeyeInstance.properties = articuloAlmacen.properties
					articuloCeyeInstance.id = articuloAlmacen.id
					articuloCeyeInstance.descripcionAlmacen = articuloCeyeInstance.desArticulo
					articuloCeyeInstance.unidadAlmacen = articuloCeyeInstance.unidad
					articuloCeyeInstance.cantidadAlmacen = 1.0
					articuloCeyeInstance.cantidadCeye = 1.0
					flash.message = "La clave no existe, capturela si es el caso"
				}			
			}	
		}
		else{
			flash.message = "No tecleo la clave"
		}
		
		render(view:"create",model:[articuloCeyeInstance: articuloCeyeInstance],params: params)
	}
		
	def filter = {
		if(!params.max) params.max = 10
		
		def articuloCeyeInstanceList = filterPaneService.filter( params, ArticuloCeye)
		
		render( view:'list',model:[articuloCeyeInstanceList: articuloCeyeInstanceList,
			articuloCeyeInstanceTotal: filterPaneService.count( params, ArticuloCeye ),
			filterParams: org.grails.plugin.filterpane.FilterPaneUtils.extractFilterParams(params),
			params:params ] )
		
	}
	   
}
