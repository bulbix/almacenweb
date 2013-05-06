package mx.gob.inr.utils

import mx.gob.inr.farmacia.ArticuloFarmacia;
import mx.gob.inr.farmacia.CatAreaFarmacia;
import mx.gob.inr.utils.Paciente;

class AutoCompleteService {

    def artlist(params)	{
		def aprox = params.term + "%"
		
		def query = {
			ilike("desArticulo", aprox)			
			maxResults(10)
		}
		
		def alist = ArticuloFarmacia.createCriteria().list(query)

		def results = alist?.collect {
			def display = String.format("(%s) %s",it.id,it.desArticulo.trim())						
			[id:it.id ,value:it.desArticulo.trim(),label:display]
		}

		return results
	}
	
	
	def arealist(params){
		
		def aprox = params.term + "%"
		
		def query = {
			or{				
				ilike("desArea", aprox)
				sqlRestriction("(cve_area || '') like '$aprox'")
			}			
			maxResults(10)
		}
		
		def alist = CatAreaFarmacia.createCriteria().list(query)

		def results = alist?.collect {
			def display = String.format("(%s) %s",it.id,it.desArea.trim())
			[id:it.id,value:display,label:display]
		}

		return results		
	}
	
	def pacientelist(params){
			
		def aprox = "%" + params.term.toUpperCase() + "%"
		
		def query = {
									
			or{
				like("numeroregistro", 'N-' + params.term + "%")				
				sqlRestriction("upper(paterno || ' ' || materno || ' ' || nombre) like '$aprox'")				
			}
			
			eq("idtipopaciente", 'N')
			order("numeroregistro", "desc")
			maxResults(10)
		}
		
		def paclist = Paciente.createCriteria().list(query)

		def results = paclist?.collect {
			def display = String.format("(%s) %s %s %s",it.numeroregistro[0..12] ,it.paterno, it.materno,it.nombre)
			[id:it.id, value:display, label:display]
		}

		return results
		
	}
	
	def stringlist(entity, campo, term){
		
		def aprox = "%" + term 
		
		log.info("stringlist " + aprox)
		
		def criteria = entity.createCriteria();
		
		def lista = criteria.list(){
			ilike(campo, aprox)
			
			projections {
				distinct (campo)
			 }
			
			maxResults(10)
		}
		
		def results = lista?.collect {
			//println it[0]
			[id: it, value: it, label: it]
		}		
		
		return results
	}
	
	
}
