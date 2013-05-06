package mx.gob.inr.farmacia

import mx.gob.inr.farmacia.EntradaFarmaciaController;
import mx.gob.inr.farmacia.EntradaFarmaciaService;
import mx.gob.inr.materiales.*

import grails.test.mixin.*
import groovy.mock.interceptor.MockFor;

import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(EntradaFarmaciaController)
@Mock([EntradaFarmaciaService,SalidaDetalleMaterial])
class EntradaControllerTests {


	
    /*void testSomething() {
       fail "Implement me"
    }*/
	
	void testConsultaMaterial(){
		
		//def entradaServiceMocker = mockFor(EntradaService)
		//controller.entradaService = entradaServiceMocker.createMock()
		params.folioAlmacen = 1
		controller.jqGridMaterial();
		
		
		
		println response.contentAsString
		
		//assert response.text == 'hola'
	}
}
