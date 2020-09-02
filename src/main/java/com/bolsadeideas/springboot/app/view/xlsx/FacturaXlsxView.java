package com.bolsadeideas.springboot.app.view.xlsx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView{

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setHeader("Content-Disposition", "attachment; filename=\"factura_view.xlsx\"");
		Factura factura = (Factura)model.get("factura"); //mismo nombre que en el controlador
		Sheet sheet = workbook.createSheet("Factura Spring");
		
		
		MessageSourceAccessor mensajes = getMessageSourceAccessor();
		
		sheet.createRow(0).createCell(0).setCellValue(mensajes.getMessage("text.factura.ver.datos.cliente"));
		sheet.createRow(1).createCell(0).setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
		sheet.createRow(2).createCell(0).setCellValue(factura.getCliente().getEmail());
		sheet.createRow(4).createCell(0).setCellValue(mensajes.getMessage("text.factura.ver.datos.factura"));
		sheet.createRow(5).createCell(0).setCellValue(mensajes.getMessage("text.cliente.factura.folio") + ": "+ factura.getId());
		sheet.createRow(6).createCell(0).setCellValue(mensajes.getMessage("text.cliente.factura.descripcion") + ": "+ factura.getDescripcion());
		sheet.createRow(7).createCell(0).setCellValue(mensajes.getMessage("text.cliente.factura.fecha") + ": "+ factura.getCreateAt());
		
		/*CellStyle theaderStyle = workbook.createCellStyle();
		theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
		theaderStyle.setBorderTop(BorderStyle.MEDIUM);
		theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
		theaderStyle.setBorderRight(BorderStyle.MEDIUM);
		
		CellStyle tbodyStyle = workbook.createCellStyle();
		tbodyStyle.setBorderBottom(BorderStyle.THIN);
		tbodyStyle.setBorderTop(BorderStyle.THIN);
		tbodyStyle.setBorderLeft(BorderStyle.THIN);
		tbodyStyle.setBorderRight(BorderStyle.THIN);*/
		
		Row header = sheet.createRow(9);
		header.createCell(0).setCellValue(mensajes.getMessage("text.factura.form.item.nombre"));
		header.createCell(1).setCellValue(mensajes.getMessage("text.factura.form.item.precio"));
		header.createCell(2).setCellValue(mensajes.getMessage("text.factura.form.item.cantidad"));
		header.createCell(3).setCellValue(mensajes.getMessage("text.factura.form.item.total"));
		
		/*header.getCell(0).setCellStyle(theaderStyle);
		header.getCell(1).setCellStyle(theaderStyle);
		header.getCell(2).setCellStyle(theaderStyle);
		header.getCell(3).setCellStyle(theaderStyle);*/
		
		int rownum = 10;
		for(ItemFactura item: factura.getItems()) {
			Row fila = sheet.createRow(rownum++);
			fila.createCell(0).setCellValue(item.getProducto().getNombre());
			fila.createCell(1).setCellValue(item.getProducto().getPrecio());
			fila.createCell(2).setCellValue(item.getCantidad());
			fila.createCell(3).setCellValue(item.calcularImporte());
		}
		
		Row filatotal = sheet.createRow(rownum);
		filatotal.createCell(2).setCellValue(mensajes.getMessage("text.factura.form.total") + ": ");
		filatotal.createCell(3).setCellValue(factura.getTotal());
		
	}

	
}
