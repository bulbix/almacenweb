package almacenweb

import static org.junit.Assert.*
import mx.gob.inr.utils.UtilService
import org.junit.*

class UtilTests {

	static transactional = false
	final int PERFIL_FARMACIA = 8
	
	UtilService utilService
	
	@Test
	void testUsuarios(){
		//log.info("Estoy en la prueba")
		def usuarios = utilService.usuarios(PERFIL_FARMACIA)
		assertEquals(5, usuarios.size())
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
