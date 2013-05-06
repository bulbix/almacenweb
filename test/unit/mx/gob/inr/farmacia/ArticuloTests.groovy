package mx.gob.inr.farmacia



import grails.test.mixin.*
import mx.gob.inr.farmacia.ArticuloFarmacia;

import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ArticuloFarmacia)
class ArticuloTests {

	
	
	void testClave(){
		def art = ArticuloFarmacia.get(272);
		//assertNull(art);
		assertEquals(art.unidad, 'PIEZAs');
		
	}
	
    void testSomething() {
       fail "Implement me"
    }
}
