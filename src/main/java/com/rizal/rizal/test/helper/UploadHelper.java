package com.rizal.rizal.test.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.rizal.rizal.test.model.Tutorial;

public class UploadHelper {
	public static String[] TYPE = { "text/csv", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" };
	static String[] HEADERs = { "Id", "Title", "Description", "Published" };
	// for excel
	static String SHEET = "Tutorials";

	public static boolean hasCSVFormat(MultipartFile file) {

		if (!TYPE[0].equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE[1].equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public static List<Tutorial> csvToTutorials(InputStream is) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<Tutorial> tutorials = new ArrayList<Tutorial>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				Tutorial tutorial = new Tutorial(Long.parseLong(csvRecord.get("Id")), csvRecord.get("Title"),
						csvRecord.get("Description"), Boolean.parseBoolean(csvRecord.get("Published")));

				tutorials.add(tutorial);
			}

			return tutorials;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
	}

	public static List<Tutorial> excelToTutorials(InputStream is) {
		try {
			Workbook workbook = new XSSFWorkbook(is);

			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheet(SHEET);
			Iterator<org.apache.poi.ss.usermodel.Row> rows = sheet.iterator();

			List<Tutorial> tutorials = new ArrayList<Tutorial>();

			Integer rowNumber = 0;
			while (rows.hasNext()) {
				org.apache.poi.ss.usermodel.Row currentRow = rows.next();

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();

				Tutorial tutorial = new Tutorial();

				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();

					switch (cellIdx) {
					case 0:
						tutorial.setId((long) currentCell.getNumericCellValue());
						break;

					case 1:
						tutorial.setTitle(currentCell.getStringCellValue());
						break;

					case 2:
						tutorial.setDescription(currentCell.getStringCellValue());
						break;

					case 3:
						tutorial.setPublished(currentCell.getBooleanCellValue());
						break;

					default:
						break;
					}

					cellIdx++;
				}

				tutorials.add(tutorial);
			}

			workbook.close();

			return tutorials;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}
}