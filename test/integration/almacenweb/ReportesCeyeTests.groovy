package almacenweb

import static org.junit.Assert.*
import mx.gob.inr.ceye.ReporteCeyeController;
import mx.gob.inr.ceye.ReporteCeyeService;
import mx.gob.inr.utils.UtilService;

import org.junit.*

class ReportesCeyeTests {
	
	ReporteCeyeService reporteCeyeService
	

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testReporteConcentradoValeEntrada() {		
		
		def parametros = [almacen:'C', fechaInicial:'01/01/2013', fechaFinal:'31/01/2013']		
		def reportList = reporteCeyeService.reporteConcentradoValeEntrada(parametros)		
		assertEquals(5, reporteList.size())
        
    }
	
	
	@Test
	void testReporteConcentradoServicioSalida() {
		
		ReporteCeyeController controller = new ReporteCeyeController()	
		
		controller.params.reportDisplay="reporteConcentradoServicioSalida"
		controller.params.methodName="reporteConcentradoServicioSalida"
		controller.params.almacen = 'C'
		controller.params.fechaInicial = '01/10/2013'
		controller.params.fechaFinal = '03/10/2013'
		controller.params.tipoVale = 'todos'
		
				
		controller.reporteDynamic() 
		
		
		
	}
}
