package mx.gob.inr.utils
import java.io.Serializable

class UsuarioPerfil implements Serializable {
	
	Usuario usuario
	Integer idPerfil
	

	static mapping = {
		table 'usuario_perfil'
		version false
		usuario column:'idusuario'
		idPerfil column:'idperfil'
		id composite: ['usuario','idPerfil']
		
	}
	
    static constraints = {
    }
}
