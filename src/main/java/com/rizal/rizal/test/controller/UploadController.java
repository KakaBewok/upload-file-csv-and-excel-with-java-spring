package com.rizal.rizal.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.rizal.rizal.test.helper.UploadHelper;
import com.rizal.rizal.test.message.ResponseMessage;
import com.rizal.rizal.test.model.Tutorial;
import com.rizal.rizal.test.service.UploadService;

@CrossOrigin("http://localhost:8080")
@Controller
@RequestMapping("/upload")
public class UploadController {

	@Autowired
	UploadService fileService;

	@PostMapping("/csv")
	public ResponseEntity<ResponseMessage> uploadFileCSV(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (UploadHelper.hasCSVFormat(file)) {
			try {
				fileService.saveCSV(file);

				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}

		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}

	@GetMapping("/csv")
	public ResponseEntity<List<Tutorial>> getAllTutorialsCSV() {
		try {
			List<Tutorial> tutorials = fileService.getAllTutorialsCSV();

			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/excel")
	public ResponseEntity<ResponseMessage> uploadFileExcel(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (UploadHelper.hasExcelFormat(file)) {
			try {
				fileService.saveExcel(file);

				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}

		message = "Please upload an excel file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}

	@GetMapping("/excel")
	public ResponseEntity<List<Tutorial>> getAllTutorialsExcel() {
		try {
			List<Tutorial> tutorials = fileService.getAllTutorialsCSV();

			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}