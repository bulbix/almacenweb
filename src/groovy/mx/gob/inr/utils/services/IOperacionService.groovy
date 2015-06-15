package mx.gob.inr.utils.services

import mx.gob.inr.seguridad.Usuario

interface IOperacionService <E> {
	
	
	long PERFIL_FARMACIA = 2
	long PERFIL_CEYE  = 10
	short AREA_FARMACIA = 6220
	
	E setJson(json, String ip, Usuario usuarioRegistro,String almacen)
	
	E guardar( E entity)
	
	def guardarDetalle(jsonDetalle, E entity, Integer renglon,String almacen)
	
	def guardarTodo(E entity, jsonArrayDetalle,String almacen)
	
	def actualizar(E entity, Long idUpdate,String almacen)
	
	def consultar(Long id)
	
	def cancelar(Long id, String almacen)
		
	def listar(params, Usuario usuarioLogueado) 
	
	def actualizarDetalle(Long id, jsonDetalle, String almacen)
	
	def borrarDetalle(Long id, Long clave, String almacen)
	
	def consultarDetalle(params)
	
	def consecutivoRenglon(E entity)
	
	def checkFolio(Integer folio,String almacen)
	
	def consecutivoFolio(String almacen)

	def usuarios(Long idPerfil)
	
	def buscarArticulo(Long clave,String almacen)
	
	def listarArticulo(String term)
	
	def listarArea(String term)
	
	def checkCierre(Date fecha, String almacen)	
	
	def reporte(Long id, String almacen)
	
}
