package com.gustavonascimento.dscatalog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gustavonascimento.dscatalog.entities.dto.CategoryDTO;
import com.gustavonascimento.dscatalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

	@Autowired
	private CategoryService service;

	@GetMapping(value = "")
	public ResponseEntity<List<CategoryDTO>> findAll() {
		List<CategoryDTO> entity = service.findAll();
		return ResponseEntity.ok(entity);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
		CategoryDTO entity = service.findById(id);
		return ResponseEntity.ok(entity);
	}
}