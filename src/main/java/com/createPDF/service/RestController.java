package com.createPDF.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
@PropertySource("classpath:application.properties")
public class RestController {

	@Autowired
	private PDFServices pdfService;

	@Value("${filePath}")
	private String filePath;

	/**
	 * 
	 * this service make a PDF of json
	 * 
	 * **/

	@RequestMapping(value = "/makePDF", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<JSONObject> makePDF(
			@RequestParam(value = "json") String json,
			HttpServletRequest request) {

		return new ResponseEntity<JSONObject>(
				pdfService.makePDF(json, request), HttpStatus.OK);

	}

	
	/** this service download the PDF file **/
	@RequestMapping(value = "/downloadPDF", method = RequestMethod.GET, produces = "application/json")
	public void makePDF(@RequestParam(value = "name") String name,
			HttpServletRequest request, HttpServletResponse response) {

		File file = new File(filePath + File.separator + name);

		if (file.exists()) {

			BufferedInputStream buffferedInPutStream = null;

			FileInputStream inStream = null;

			try {

				response.reset();
				
				response.setContentType("application/pdf");

				inStream = new FileInputStream(file);

				buffferedInPutStream = new BufferedInputStream(inStream);

				int i;

				while ((i = buffferedInPutStream.read()) != -1) {

					response.getOutputStream().write(i);
				}
				
				
				buffferedInPutStream.close();
				
				inStream.close();
				
				response.flushBuffer();
								
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
