package mx.gob.inr.utils.services

import mx.gob.inr.seguridad.Usuario

interface IOperacionService <E> {
	
	
	long PERFIL_FARMACIA = 8
	long PERFIL_CEYE  = 10
	short AREA_FARMACIA = 6220
	
	E setJson(json, String ip, Usuario usuarioRegistro)
	
	E guardar( E entity)
	
	def guardarDetalle(jsonDetalle, E entity, Integer renglon)
	
	def guardarTodo(E entity, jsonArrayDetalle)
	
	def actualizar(E entity, Long idUpdate)
	
	def consultar(Long id)
	
	def cancelar(Long id)
		
	def listar(params) 
	
	def actualizarDetalle(Long id, jsonDetalle)
	
	def borrarDetalle(Long id, Long clave)
	
	def consultarDetalle(params)
	
	def consecutivoRenglon(E entity)
	
	def checkFolio(Integer folio)
	
	def consecutivoFolio()

	def usuarios(Long idPerfil)
	
	def buscarArticulo(Long clave)
	
	def listarArticulo(String term)
	
	def listarArea(String term)
	
	def checkCierre(Date fecha)	
	
	def reporte(Long id)
	
}
