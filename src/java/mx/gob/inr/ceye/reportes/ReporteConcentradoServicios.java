package mx.gob.inr.ceye.reportes;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import mx.gob.inr.utils.UtilService;
import mx.gob.inr.utils.domain.CatArea;
import mx.gob.inr.utils.reportes.ConcentradoServicio;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class ReporteConcentradoServicios extends PdfPageEventHelper implements Serializable {
	
	UtilService utilService;
	String imageDir;
	
	DecimalFormat dos = new DecimalFormat(".00");
	BaseFont baseFont;	
	
	
	public ReporteConcentradoServicios(UtilService utilService, String imageDir){
		this.utilService = utilService;
		this.imageDir = imageDir;
	}
	
	public byte[] generarReporte(List<ConcentradoServicio> lista,
			String rango, List<CatArea> areas, String almacen, String tipoVale) {			
		
		HashMap saldos = new HashMap();
		HashMap<Long,Integer> consumos = new HashMap<Long,Integer>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		ConcentradoServicio vo = new ConcentradoServicio();
		
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LETTER.rotate(), 10, 10, 10, 10);

		try {
			
			String logoPath = imageDir + "logotipo.jpg";			
			
			PdfWriter writer = PdfWriter.getInstance(document, stream);
			baseFont = BaseFont.createFont(BaseFont.TIMES_ROMAN,
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			addProperties(document);

			writer.setPageCount(1);
			writer.setPageEvent(this);
			document.open();

			Image inrlogo = Image.getInstance(logoPath);
			Font subs, font, fontDatos, fontTabla, fontPartida;
			subs = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD);
			font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD);
			fontDatos = FontFactory.getFont(FontFactory.TIMES_ROMAN, 7,
					Font.NORMAL);
			fontTabla = FontFactory.getFont(FontFactory.TIMES_ROMAN, 6,
					Font.BOLD);
			fontPartida = FontFactory.getFont(FontFactory.TIMES_ROMAN, 6,
					Font.BOLD);

			PdfPTable datos = new PdfPTable(1);
			datos.setWidthPercentage(100);
			datos.getDefaultCell().setBorder(0);
			datos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			datos.addCell(new Paragraph("INSTITUTO NACIONAL DE REHABILITACION",
					font));
			datos.addCell(new Paragraph("", subs));
			datos.addCell(new Paragraph(
					"DEPARTAMENTO DE ALMACENES Y CONTROL DE ALMACENES", subs));
			datos.addCell(new Paragraph("\nALMACEN DE " + utilService.getAlmacenDescripcion(almacen), subs));
			datos.addCell(new Paragraph(rango.toUpperCase() + "\n\n", fontDatos));

			float encabezadoWidth[] = { 3, 7, 20, 40, 10, 18, 2 };
			PdfPTable encabezado = new PdfPTable(encabezadoWidth);
			encabezado.setWidthPercentage(100);
			encabezado.getDefaultCell().setBorder(0);
			encabezado.addCell("");
			encabezado.addCell(inrlogo);
			encabezado.addCell("");
			encabezado.addCell(datos);
			encabezado.addCell("");
			encabezado.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_RIGHT);
			encabezado.addCell(new Paragraph("\n\nFECHA: " + utilService.getFechaActual("dd/MM/yyyy HH:mm"), fontDatos));
			encabezado.addCell("");

			float indicadoresWidth[] = { 25, 25, 25, 25 };
			PdfPTable indicadores = new PdfPTable(indicadoresWidth);
			indicadores.setWidthPercentage(100);
			indicadores.getDefaultCell().setBorder(0);
			
			
			//Para inicializar los tamanios dependiendien el almacen
			float tablaWidth[] = new float[4 + areas.size()];
			tablaWidth[0] = 5; 
			tablaWidth[1] = 18;
			tablaWidth[2] = 8;
			int index = 3;
			
			for(CatArea area:areas)
				tablaWidth[index++] = 3;

			tablaWidth[tablaWidth.length -1] = 3;
			
			
			
			PdfPTable tablaColumnas = new PdfPTable(tablaWidth);
			tablaColumnas.setWidthPercentage(100);
			tablaColumnas.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			tablaColumnas.getDefaultCell().setBorderWidth(1);
			tablaColumnas.getDefaultCell().setBackgroundColor(Color.LIGHT_GRAY);
			tablaColumnas.addCell(new Paragraph("CLAVE", fontTabla));
			tablaColumnas.addCell(new Paragraph("DESCRIPCION", fontTabla));
			tablaColumnas.addCell(new Paragraph("U. MEDIDA", fontTabla));
			
			//Para cargar las cabeceras
			for(CatArea area:areas)			
				tablaColumnas.addCell(new Paragraph(area.getId() + " " + area.getDesArea() , fontTabla));
			
			tablaColumnas.addCell(new Paragraph("Total", fontTabla));
			tablaColumnas.getDefaultCell().setBackgroundColor(Color.WHITE);
			
			PdfPTable tabla = new PdfPTable(tablaWidth);
			tabla.setWidthPercentage(100);
			tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			tabla.getDefaultCell().setBorderWidth(1);

			long area = 0;
			int x = 0;
			Integer suma = 0;
			Integer valor = 0;			
			Integer totalConsumoFinal = 0;
			Double totalImporteFinal = 0.0;

			String id = "";
			String var = "";
			String tipoEntrada = "TODAS";						
			String auxiliar = "";
			String clave, descripcion, unidad;			

			while (x < lista.size()) {
				vo = lista.get(x);
				
				id = vo.getCveArt().toString();
				clave = vo.getCveArt().toString();
				descripcion = vo.getDescArticulo();
				unidad = vo.getUnidad();				

				while (vo.getCveArt().toString().equals(id) && x < lista.size()) {

					if(vo.getCveArea() != null)
						area = vo.getCveArea();					

					try {
						suma = vo.getCantidad().intValue();
					} catch (Exception ex) {
						suma = 0;
					}

					try {
						var = consumos.get(area).toString();
						valor = Integer.parseInt(var);
					} catch (Exception ex) {
						valor = 0;
					}

					suma += valor;					
					consumos.put(area, suma);

					x++;
					if (x < lista.size())
						vo = lista.get(x);
				}

				Integer totalArea = llenarCeldas(tabla, vo, consumos, clave, descripcion, unidad, fontTabla,areas);
				consumos.clear();
				saldos.clear();

				totalConsumoFinal += totalArea;
			}
			tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			tabla.getDefaultCell().setBorderWidth(0);
			
			for (int f = 1; f <= areas.size() + 2; f++)
				tabla.addCell(new Paragraph("", fontTabla));
			tabla.addCell(new Paragraph("Total:", fontTabla));
			tabla.addCell(new Paragraph(totalConsumoFinal.toString(),fontTabla));	

			llenarEncabezado(encabezado, tipoVale, writer,	fontDatos);

			PdfPTable tablaContent = new PdfPTable(1);
			tablaContent.getDefaultCell().setBorderWidth(0);
			tablaContent.setWidthPercentage(100);
			tablaContent.addCell(tabla);			

			PdfPTable principal = new PdfPTable(1);
			principal.setWidthPercentage(100);
			principal.getDefaultCell().setBorder(0);
			principal.addCell(encabezado);
			principal.addCell(tablaColumnas);
			principal.addCell(tablaContent);			
			principal.setHeaderRows(2);
			
			document.add(principal);
			
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			document.close();		
		}

		return stream.toByteArray();
	}

	public void llenarEncabezado(PdfPTable encabezado, String tipoVale, PdfWriter writer, Font fontDatos) {
		encabezado.addCell("");
		encabezado.addCell("");
		encabezado.addCell(new Paragraph("TIPO: " + tipoVale.toUpperCase(), fontDatos));
		encabezado.addCell("");
		encabezado.addCell(new Paragraph("HOJA:  ", fontDatos));
		encabezado.addCell("");
		encabezado.addCell("");
		encabezado.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

	}

	public Integer llenarCeldas(PdfPTable tabla, ConcentradoServicio vo,
			HashMap<Long,Integer> consumos,String clave, String descripcion,
			String unidad, Font fontTabla, List<CatArea> areas) {

		Integer totalCantidades = 0;		
		Integer cveArea = 0;

		tabla.addCell(new Paragraph("" + clave, fontTabla));
		tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		tabla.addCell(new Paragraph("" + descripcion, fontTabla));
		tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla.addCell(new Paragraph("" + unidad, fontTabla));
		
		for (CatArea area: areas) {
			if (consumos.get(area.getId()) == null) //No hubo cantidades para esos meses
				tabla.addCell(new Paragraph("0", fontTabla));
			else {
				cveArea = Integer.parseInt(consumos.get(area.getId()).toString());
				tabla.addCell(new Paragraph("" + cveArea, fontTabla));
				totalCantidades += Integer.parseInt(consumos.get(area.getId()).toString());
				//totalSaldos += Double.parseDouble(saldo.get(i).toString());
			}
		}	

		tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla.addCell(new Paragraph(totalCantidades.toString(), fontTabla));
		tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		return totalCantidades;
	}

	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		cb.setFontAndSize(baseFont, 10);
		cb.beginText();
		//cb.showTextAligned(Element.ALIGN_RIGHT,writer.getPageNumber()+"",300f, 62f, 0);
		cb.showTextAligned(Element.ALIGN_RIGHT,writer.getPageNumber()+"",650,550,0);
		cb.endText();
	}

	public void addProperties(Document doc) {
		doc.addTitle("Pdf de Ceye");
		doc.addSubject("Reporte de Concentrado Servicios");
		doc.addKeywords("Itext API");
		doc.addCreator("INR");
		doc.addAuthor("Luis Prado");
	}
	
	

}
