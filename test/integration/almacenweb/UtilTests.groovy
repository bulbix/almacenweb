package almacenweb

import grails.plugins.springsecurity.SpringSecurityService
import  mx.gob.inr.farmacia.*
import static org.junit.Assert.*
import mx.gob.inr.utils.UtilService
import org.junit.*

class UtilTests {

	static transactional = false
	final int PERFIL_FARMACIA = 8
	
	
	SpringSecurityService springSecurityService
	UtilService utilService
	
	@Test
	void encodePassword(){
		String password = 'e'
		assertEquals(springSecurityService.encodePassword(password),"huevos")
	}
	
	
	
	@Test
	void testUsuarios(){
		//log.info("Estoy en la prueba")
		def usuarios = utilService.usuarios(PERFIL_FARMACIA)
		assertEquals(5, usuarios.size())
	}
	
	@Test
	void testConsultar() {
		
		def all = EntradaFarmacia.findAll()
		
		//def list = Entrada.list()
		//def entrada = Entrada.findByNumeroEntrada(50)
	   
		//assertEquals(1,all[0].numeroEntrada)
	   
		assertNotNull(all)
		assertEquals(all.size(), 1)
	}
	
	
	@Test
	void testFechaDesglosada(){
		def result = utilService.fechaDesglosada(new Date())
		assertEquals(2012, result.anio)
	}
	
	
    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    
}
