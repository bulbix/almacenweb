package mx.gob.inr.utils

interface IOperacionController {
	
	def index()
	def create(Integer id)
	def list(Integer max)
	
	
	def eliminar(Integer id)
	
	
	def guardar()
	def guardarTodo()
	def actualizar()
	def cancelar()
	
	def consultarDetalle()
	def actualizarDetalle(params)
	
	def uniqueFolio()
	def buscarArticulo()
	def listarArticulo()
	def listarArea()
	
	def checkCierre()
	
	def reporte()	
}
