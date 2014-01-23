package mx.gob.inr.ceye.reportes;

import java.awt.Color;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.style.SimpleStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.Styles;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;
import mx.gob.inr.utils.UtilService;
import mx.gob.inr.utils.domain.CatArea;
import mx.gob.inr.utils.reportes.ConcentradoServicio;

public class ReporteDynamicConcentradoServicios {
	
	
	UtilService utilService;
	String imageDir;
	HttpServletResponse response;
	
	
	
	public ReporteDynamicConcentradoServicios(UtilService utilService,
			String imageDir, HttpServletResponse response) {
		super();
		this.utilService = utilService;
		this.imageDir = imageDir;
		this.response = response;
	}
	
	public void generarReporte(List<ConcentradoServicio> lista,
			String rango, List<CatArea> areas, String almacen,String tipoVale, String tipoImpresion) {
		
		
		JasperReportBuilder report = report();//a new report
		
		
				
		StyleBuilder fontStyle = stl.style().setFontSize(7);
		StyleBuilder fontTitleStyle = stl.style().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.CENTER).setBold(true);
		
		TextColumnBuilder<Integer> cveArtColumn  = col.column("Clave", "cveArt", type.integerType())
				.setWidth(7).setStyle(fontStyle).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(fontTitleStyle);
		TextColumnBuilder<String> descripcionColumn  = col.column("Descripcion", "descArticulo", type.stringType())
				.setWidth(30).setStyle(fontStyle).setStretchWithOverflow(true).setTitleStyle(fontTitleStyle);
		
		TextColumnBuilder<String> unidadColumn  = col.column("Unidad", "unidad", type.stringType())
				.setWidth(10).setStyle(fontStyle).setStretchWithOverflow(true).setTitleStyle(fontTitleStyle);
		
		report.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
		  .columns(cveArtColumn,descripcionColumn,unidadColumn);
		
		
		for(CatArea area:areas){
			
			TextColumnBuilder<Integer> cantidadColumn  = col.column(area.toString(), "cantidad_area" + area.getId(), type.integerType())
					.setWidth(6).setStyle(fontStyle).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(fontTitleStyle);
			
			report.columns(cantidadColumn);
		}
		
		TextColumnBuilder<Integer> totalColumn  = col.column("Total", "sumaCantidadArea", type.integerType())
				.setWidth(6).setStyle(fontStyle).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(fontTitleStyle);
		
		report.columns(totalColumn);
				
		Color colorLightGray = new Color(243, 243, 243);
		SimpleStyleBuilder evenRowColor = Styles.simpleStyle().setBackgroundColor(colorLightGray);
		report.setDetailEvenRowStyle(evenRowColor);	
		
		String titulo = "INSTITUTO NACIONAL DE REHABILITACION"+
				"\nDEPARTAMENTO DE ALMACENES Y CONTROL DE ALMACENES\n"+
				"\nALMACEN DE " + utilService.getAlmacenDescripcion(almacen) +
				"\n" + rango.toUpperCase(); 
		
		
		report.title(Components.text(titulo)			  
			  .setHorizontalAlignment(HorizontalAlignment.CENTER))
			  .pageFooter(Components.pageXofY())//show page number on the page footer
			  .setDataSource(lista);
		
		
		try {
			
			OutputStream out = response.getOutputStream();
			
			if(tipoImpresion.equals("pdf")){
				response.setContentType("application/pdf");
				report.toPdf(out);
			}
			if(tipoImpresion.equals("excel")){
				response.setContentType("application/vnd.ms-excel");
				report.toXls(out);
			}	
			
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			
		}
	}

}
