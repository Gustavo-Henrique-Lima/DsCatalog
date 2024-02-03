package com.gustavonascimento.dscatalog.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gustavonascimento.dscatalog.entities.dto.ProductDTO;
import com.gustavonascimento.dscatalog.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

	@Autowired
	private ProductService service;

	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
		Page<ProductDTO> entity = service.findAllPaged(pageable);
		return ResponseEntity.ok(entity);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
		ProductDTO entity = service.findById(id);
		return ResponseEntity.ok(entity);
	}

	@PostMapping
	public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO entity) {
		entity = service.insert(entity);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entity.getId()).toUri();
		return ResponseEntity.created(uri).body(entity);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO entity) {
		entity = service.update(id, entity);
		return ResponseEntity.ok(entity);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}