package almacenweb

import mx.gob.inr.farmacia.CatAreaFarmacia;
import mx.gob.inr.ceye.CatAreaCeye;
import mx.gob.inr.utils.Partida;

class AlmacenTagLib {
	
	
	def utilService
	
	def almacenDescripcion = { attrs, body ->
		
		def almacen = attrs.almacen		
		def descripcion = utilService.getAlmacenDescripcion(almacen)
		def code = attrs.code
		
		out << g.message(code:code, args:[descripcion])		
	}
	
	
	def areaList ={ attrs, body ->
		
		def almacen = attrs.almacen
		
		def areaList = []
		
		if(almacen == 'F')
			areaList = CatAreaFarmacia.listOrderByDesArea()
		else
			areaList = CatAreaCeye.listOrderByDesArea()
			
		out << """<label for="partida">Area</label>"""
			
		out << g.select( name:"area", from:areaList, optionKey:"id" ,
			optionValue:"desArea", noSelection:"${['':'SELECCIONE AREA']}")	
			
	}
	
	def partidaList = { attrs, body ->
		def partidaList = Partida.listOrderByDesPart()
		
		out << """	<label for="partida">Area</label> """
		
		out << g.select( name:"partida", from:partidaList, optionKey:"partida" , 
			optionValue:"desPart", noSelection:"${['':'SELECCIONE PARTIDA']}")
		
	}

}
