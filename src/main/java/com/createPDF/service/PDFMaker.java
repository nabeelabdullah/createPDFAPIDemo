package com.createPDF.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Iterator;

/**
 *
 * @author NABEEL
 */
@Service
@PropertySource("classpath:application.properties")
public class PDFMaker {

	@Value("${filePath}")
	private String filePath;

	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.BOLD);

	private static Font timeFont = new Font(Font.FontFamily.TIMES_ROMAN, 9);

	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
			Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.NORMAL);


	private PdfPTable tableCreater(int size, int a[]) throws DocumentException {

		PdfPTable table = new PdfPTable(size);

		table.setWidths(a);

		table.setWidthPercentage(100);

		return table;
	}

	private void cellCreater(PdfPTable table) {

		PdfPCell pc = new PdfPCell(new Phrase("UserId", catFont));
		pc.setPadding(4);
		pc.setVerticalAlignment(Element.ALIGN_CENTER);
		pc.setHorizontalAlignment(Element.ALIGN_CENTER);
		pc.setBackgroundColor(new BaseColor(204, 204, 204));
		table.addCell(pc);

		pc = new PdfPCell(new Phrase("User Name", catFont));
		pc.setPadding(4);
		pc.setVerticalAlignment(Element.ALIGN_CENTER);
		pc.setHorizontalAlignment(Element.ALIGN_CENTER);
		pc.setBackgroundColor(new BaseColor(204, 204, 204));
		table.addCell(pc);

		pc = new PdfPCell(new Phrase("Company", catFont));
		pc.setPadding(4);
		pc.setVerticalAlignment(Element.ALIGN_CENTER);
		pc.setHorizontalAlignment(Element.ALIGN_CENTER);
		pc.setBackgroundColor(new BaseColor(204, 204, 204));
		table.addCell(pc);

		pc = new PdfPCell(new Phrase("CTC", catFont));
		pc.setPadding(4);
		pc.setVerticalAlignment(Element.ALIGN_CENTER);
		pc.setHorizontalAlignment(Element.ALIGN_CENTER);
		pc.setBackgroundColor(new BaseColor(204, 204, 204));
		table.addCell(pc);

	}

	private void addRecordInTable(PdfPTable table, String value) {

		PdfPCell pc = new PdfPCell(new Phrase(value, smallBold));

		pc.setPadding(5);

		pc.setVerticalAlignment(Element.ALIGN_CENTER);

		table.addCell(pc);

	}

	private void title(Document document, String title) throws DocumentException {
		Paragraph ph = new Paragraph();
		Chunk c1 = new Chunk(title, catFont);
		ph.add(c1);
		ph.setSpacingAfter(2);
		document.add(ph);

	}

	private void makeCreateDate(Document document) throws DocumentException {

		Paragraph ph = new Paragraph();

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		Calendar cal = Calendar.getInstance();

		Chunk c1 = new Chunk("Created On " + dateFormat.format(cal.getTime()),
				timeFont);

		ph.add(c1);

		ph.setSpacingAfter(2);

		ph.setAlignment(Element.ALIGN_RIGHT);

		document.add(ph);

	}

	private void space(Document document) throws DocumentException {

		Paragraph p = new Paragraph(" ");

		document.add(p);
	}

	public String make(JSONObject object) throws FileNotFoundException,
			DocumentException, InvalidDataException {

		Document document = new Document(PageSize.A4, 0, 0, 0, 0);

		String pdfName = Calendar.getInstance().getTimeInMillis() + ".pdf";

		document.setMargins(5, 5, 1, 1);

		PdfWriter.getInstance(document, new FileOutputStream(filePath
				+ File.separator + pdfName));

		document.open();

		makeCreateDate(document);

		Paragraph p = new Paragraph("Assignment", subFont);

		p.setAlignment(Element.ALIGN_CENTER);

		document.add(p);

		JSONArray userDetailArr = (JSONArray) object.get("userDetail");

		if (userDetailArr != null) {

			/** create table of size 4 **/
			int a[] = { 200, 400, 400, 200 };
			
			PdfPTable table = tableCreater(4, a);
			

			title(document, "User Detail");

			space(document);

			cellCreater(table);

			Iterator userIt = userDetailArr.iterator();

			while (userIt.hasNext()) {

				JSONObject useObject = (JSONObject) userIt.next();

				if (useObject.get("id") == null
						|| useObject.get("name") == null
						|| useObject.get("company") == null
						|| useObject.get("ctc") == null) {

					throw new InvalidDataException();

				}

				addRecordInTable(table, useObject.get("id").toString());

				addRecordInTable(table, useObject.get("name").toString());

				addRecordInTable(table, useObject.get("company").toString());

				addRecordInTable(table, useObject.get("ctc").toString());

			}

			document.add(table);

		}

		document.close();

		return pdfName;

	}
	
	
	
	
	
	
	

}