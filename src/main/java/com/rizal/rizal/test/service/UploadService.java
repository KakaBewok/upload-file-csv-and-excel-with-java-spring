package com.rizal.rizal.test.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rizal.rizal.test.helper.UploadHelper;
import com.rizal.rizal.test.model.Tutorial;
import com.rizal.rizal.test.repository.TutorialRepository;

@Service
public class UploadService {
	@Autowired
	TutorialRepository repository;

	public void saveCSV(MultipartFile file) {
		try {
			List<Tutorial> tutorials = UploadHelper.csvToTutorials(file.getInputStream());
			repository.saveAll(tutorials);
		} catch (IOException e) {
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
	}

	public List<Tutorial> getAllTutorialsCSV() {
		return repository.findAll();
	}

	public void saveExcel(MultipartFile file) {
		try {
			List<Tutorial> tutorials = UploadHelper.excelToTutorials(file.getInputStream());
			repository.saveAll(tutorials);
		} catch (IOException e) {
			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}

	public List<Tutorial> getAllTutorialsExcel() {
		return repository.findAll();
	}
}