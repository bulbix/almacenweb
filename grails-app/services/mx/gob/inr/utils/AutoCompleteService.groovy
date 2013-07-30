package mx.gob.inr.utils

class AutoCompleteService {

    def listarArticulo(String term, entityArticulo)	{
	   def aprox = term + "%"
	   
	   def query = {
		   ilike("desArticulo", aprox)
		   maxResults(10)
		   order("desArticulo","asc")
	   }
	   
	   def alist = entityArticulo.createCriteria().list(query)

	   def results = alist?.collect {
		   def display = String.format("(%s) %s",it.id,it.desArticulo.trim())
		   [id:it.id ,value:it.desArticulo.trim(),label:display]
	   }

	   return results
   }
   
   
   def listarArea(String term, entityArea){
	   
	   def aprox = term + "%"
	   
	   def query = {
		   or{
			   ilike("desArea", aprox)
			   sqlRestriction("(cve_area || '') like '$aprox'")
		   }
		   maxResults(10)
		   order("desArea","asc")
	   }
	   
	   def alist = entityArea.createCriteria().list(query)

	   def results = alist?.collect {
		   def display = String.format("(%s) %s",it.id,it.desArea.trim())
		   [id:it.id,value:display,label:display]
	   }

	   return results
   }
   
   def listarProcedimiento(String term){
	   
	   def aprox = term + "%"
	   
	   def query = {
		   or{
			   ilike("descdiag", aprox)
			   sqlRestriction("(iddiagnostico || '') like '$aprox'")
		   }
		   maxResults(10)
		   order("descdiag","asc")
	   }
	   
	   def alist = Cie09.createCriteria().list(query)

	   def results = alist?.collect {
		   def display = String.format("(%s) %s",it.id,it.descdiag.trim())
		   [id:it.id,value:display,label:display]
	   }

	   return results
   }
   
   def listarPaciente(String term){
		   
	   def aprox = "%" + term.toUpperCase() + "%"
	   
	   def query = {
								   
		   or{
			   like("numeroregistro", 'N-' + term + "%")
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
   
   def listarNombre(entity, campo, term){
	   
	   def aprox = term + "%"
	   
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
