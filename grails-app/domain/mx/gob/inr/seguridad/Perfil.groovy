package mx.gob.inr.seguridad

class Perfil {

	Long id
	String authority

	static mapping = {
		id column:'idperfil'
		authority column:'desc_perfil'
		version false
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
