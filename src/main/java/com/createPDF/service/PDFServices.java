package com.createPDF.service;

import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

@Service
public class PDFServices {

	@Autowired
	private PDFMaker pdfMaker;

	public JSONObject makePDF(String JSON, HttpServletRequest request) {

		JSONObject result = new JSONObject();

		try {

			JSONParser parser = new JSONParser();

			JSONObject object = (JSONObject) parser.parse(JSON);

			String fileName = pdfMaker.make(object);

			/**
			 * If file created success
			 * **/

			result.put("response", "success");

			result.put("file", fileName);

		} catch (ParseException e) {

			/**
			 * If coming String not a JSON
			 * **/

			result.put("response", "jsonInvalid");

			e.printStackTrace();

		} catch (FileNotFoundException e) {

			/**
			 * If file location not found (this file location can be set in
			 * application.properties file with var name 'filePath')
			 * **/

			result.put("response", "fileNotFound");

			e.printStackTrace();

		} catch (DocumentException e) {

			/**
			 * fail to make a PDF
			 * **/

			result.put("response", "creationFailed");

			e.printStackTrace();

		} catch (InvalidDataException e) {

			/**
			 * coming JSON not fulfill format conditions
			 * **/

			result.put("response", "formatIncorrect");

			e.printStackTrace();
		}

		return result;

	}

}
