package com.gustavonascimento.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavonascimento.dscatalog.entities.Category;
import com.gustavonascimento.dscatalog.entities.dto.CategoryDTO;
import com.gustavonascimento.dscatalog.repositories.CategoryRepository;
import com.gustavonascimento.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> objs = repository.findAll();
		List<CategoryDTO> entities = objs.stream().map(x -> new CategoryDTO(x)).toList();
		return entities;
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
		return new CategoryDTO(entity);
	}
}