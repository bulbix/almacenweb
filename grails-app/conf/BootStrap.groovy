import mx.gob.inr.seguridad.Usuario

class BootStrap {

    def init = { servletContext ->
		
		//def testUser = new Usuario(id:9000,username: 'me',password: 'e')
		//testUser.save(flush: true)
		
		//UserioPerfil.create testUser, adminRole, true
    }
    def destroy = {
    }
}
