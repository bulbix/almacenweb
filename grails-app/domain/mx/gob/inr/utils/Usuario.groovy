package mx.gob.inr.utils

class Usuario {
	
	String rfc
	String nombre
	String paterno
	String materno
	String password
	
	static hasMany = [perfiles: UsuarioPerfil]
	
    static mapping = {
		id column:'idusuario'
		version false
    
	}
	
	String toString(){
		return rfc
	}
	
	
}