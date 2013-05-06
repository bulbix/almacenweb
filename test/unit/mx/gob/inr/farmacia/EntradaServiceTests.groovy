package mx.gob.inr.farmacia

import mx.gob.inr.farmacia.*;
import mx.gob.inr.farmacia.EntradaFarmaciaService;
import mx.gob.inr.materiales.SalidaDetalleMaterial
import mx.gob.inr.utils.*;

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(EntradaFarmaciaService)
@Mock([SalidaDetalleMaterial,EntradaFarmacia,EntradaDetalleFarmacia,UtilService])
class EntradaServiceTests {		
	
    /*void testConsultar() {

		def result = service.consultarEntrada(56);		
		assertNotNull(result)
		//assertEquals(2, result.id)
    }*/
	
	
	def UtilService utilService
	
	
	void testConsultaMaterial(){
		
		service.utilService = utilService 
		
		def detalle = service.consultaMaterial(1)
		
		assertEquals(4, detalle.rows.size())
		//assertNotNull(detalle)
	}
	
	void testCheckFolio(){
		
	}
	
	void testDiponibilidad(){
		def existencia = service.disponibilidadArticulo(3, new Date())
		assertEquals(existencia,15)
	}
	
}
