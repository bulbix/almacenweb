package mx.gob.inr.ceye.reportes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.jasper.builder.JasperConcatenatedReportBuilder;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import mx.gob.inr.ceye.CatAreaCeye;
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
	
	private JasperReportBuilder reporteArchivo(List<ConcentradoServicio> lista, String almacenAreaCeye,Map params){
		
		JasperReportBuilder report = report();//a new report	
		
		StyleBuilder fontStyle = stl.style().setFontSize(7).setBorder(stl.penThin());
		StyleBuilder fontTitleStyle = stl.style(fontStyle).
				setHorizontalAlignment(HorizontalAlignment.CENTER).setBold(true).setBackgroundColor(Color.LIGHT_GRAY);;
		
		TextColumnBuilder<Integer> cveArtColumn  = col.column("Clave", "cveArt", type.integerType())
				.setWidth(7).setStyle(fontStyle).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(fontTitleStyle);
		TextColumnBuilder<String> descripcionColumn  = col.column("Descripcion", "descArticulo", type.stringType())
				.setWidth(30).setStyle(fontStyle).setStretchWithOverflow(true).setTitleStyle(fontTitleStyle);
		
		TextColumnBuilder<String> unidadColumn  = col.column("Unidad", "unidad", type.stringType())
				.setWidth(10).setStyle(fontStyle).setStretchWithOverflow(true).setTitleStyle(fontTitleStyle);
		
		report.setPageFormat(PageType.LETTER, PageOrientation.LANDSCAPE).columns(cveArtColumn,descripcionColumn,unidadColumn);		
		
		List<CatAreaCeye> areas = utilService.areasCeyeByAlmacen(almacenAreaCeye);		
		
		
		TextColumnBuilder<BigDecimal> totalColumn = col.column("Total", "total", type.bigDecimalType());
				
				
		for(CatArea area:areas){			
			TextColumnBuilder<Integer> cantidadColumn  = col.column(area.getId() + " " + area.getDesArea(), "cantidad_area" + area.getId(), type.integerType())
					.setWidth(6).setStyle(fontStyle).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(fontTitleStyle);			
			report.columns(cantidadColumn);
			
			totalColumn = totalColumn.add(cantidadColumn);	
			
		}
		
		totalColumn.setWidth(6).setStyle(fontStyle).setHorizontalAlignment(HorizontalAlignment.CENTER)
			.setTitleStyle(fontTitleStyle).setTitle("Total").setPattern("###0");
		
		report.columns(totalColumn);
		
		
		String rango = params.get("reportDisplay")  + "DEL"  +params.get("fechaInicial") + " AL " + params.get("fechaFinal");		
		
		String titulo = "INSTITUTO NACIONAL DE REHABILITACION"+
				"\nDEPARTAMENTO DE ALMACENES Y CONTROL DE ALMACENES\n"+
				"\nALMACEN DE " + utilService.getAlmacenDescripcion(params.get("almacen").toString()) + " -- AREA DE " + utilService.getAlmacenDescripcion(almacenAreaCeye)+
				"\n" + rango.toUpperCase(); 
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(imageDir+"logotipo.jpg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		report.pageHeader(
				cmp.horizontalList().add(
				cmp.image(img).setFixedDimension(80, 80).setHorizontalAlignment(HorizontalAlignment.LEFT),	
				cmp.text(titulo).setHorizontalAlignment(HorizontalAlignment.CENTER)
				),
				
				cmp.horizontalList().add(
				cmp.text("Tipo Vale:" + params.get("tipoVale").toString().toUpperCase()),
				cmp.text((new Date())).setHorizontalAlignment(HorizontalAlignment.RIGHT),
				cmp.pageXofY().setHorizontalAlignment(HorizontalAlignment.RIGHT)
				)
			)			
			.highlightDetailEvenRows()		 		  
			.setDataSource(lista);
		
		return report;		
		
	}
	
	/****
	 * Genera un archivo zip con los tres reportes de areas 
	 * @param lista
	 * @param params
	 */
	public void generarReporte(List<ConcentradoServicio> lista, Map params) {	
		
		try {				
			JasperConcatenatedReportBuilder report = concatenatedReport().continuousPageNumbering().concatenate(
					reporteArchivo(lista, "C", params),
					reporteArchivo(lista, "S", params),
					reporteArchivo(lista, "Q", params));

			OutputStream out = response.getOutputStream();
			String tipoImpresion  = params.get("tipoImpresion").toString();

			if(tipoImpresion.equals("pdf")){
				response.setHeader("Content-disposition", "attachment;filename="  + params.get("reportDisplay") + ".pdf");
				response.setContentType("application/pdf");
				report.toPdf(out);
			}
			if(tipoImpresion.equals("excel")){
				response.setHeader("Content-disposition", "attachment;filename="  + params.get("reportDisplay") + ".xls");
				response.setContentType("application/vnd.ms-excel");
				report.toXls(out);
			}	

			out.close();		
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}

}
