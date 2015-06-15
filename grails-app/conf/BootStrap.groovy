import mx.gob.inr.seguridad.*

class BootStrap {

    def init = { servletContext ->
		
		def usuario = new Usuario(id:1, username:'PAFL0000',
			password:'garbage', cedula:'11111', nombre:'Luis Prado')
			usuario.save()
			def perfil = new Perfil(id:8, authority:'ROLE_FARMACIA')
			perfil.save()
			(new UsuarioPerfil(usuario:usuario, perfil:perfil)).save()
    }
    def destroy = {
    }
}
