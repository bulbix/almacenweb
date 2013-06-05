package almacenweb

class AlmacenTagLib {
	
	
	def utilService
	
	def almacenDescripcion = { attrs, body ->
		
		def almacen = attrs.almacen		
		def descripcion = utilService.getAlmacenDescripcion(almacen)
		def code = attrs.code
		
		out << g.message(code:code, args:[descripcion])
		
	}

}
