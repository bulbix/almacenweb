package mx.gob.inr.farmacia



import grails.test.mixin.*
import mx.gob.inr.farmacia.EntradaFarmacia;

import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(EntradaFarmacia)
@Mock(EntradaFarmacia)
class EntradaTests {

	
	/*void testGuardar(){
		
		def entrada = new Entrada()
		entrada.numeroEntrada = 1
		
		entrada.save();
		
		
	}*/
	
	
    void testConsultar() {
		
		def all = EntradaFarmacia.findAll()
		
		//def list = Entrada.list()
		//def entrada = Entrada.findByNumeroEntrada(50)
	   
		//assertEquals(1,all[0].numeroEntrada)
	   
		assertNotNull(all)
		assertEquals(all.size(), 1)
    }
}
