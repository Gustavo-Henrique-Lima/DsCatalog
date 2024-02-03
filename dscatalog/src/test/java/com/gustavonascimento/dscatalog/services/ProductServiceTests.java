package com.gustavonascimento.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavonascimento.dscatalog.entities.Category;
import com.gustavonascimento.dscatalog.entities.Product;
import com.gustavonascimento.dscatalog.entities.dto.ProductDTO;
import com.gustavonascimento.dscatalog.repositories.CategoryRepository;
import com.gustavonascimento.dscatalog.repositories.ProductRepository;
import com.gustavonascimento.dscatalog.services.exceptions.DataBaseException;
import com.gustavonascimento.dscatalog.services.exceptions.ResourceNotFoundException;
import com.gustavonascimento.dscatalog.tests.CategoryFactory;
import com.gustavonascimento.dscatalog.tests.ProductFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	@Mock
	private CategoryRepository categoryRepository;

	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private PageImpl<Product> page;
	private Product entity;
	private Category entityCategory;
	private ProductDTO entityDTO;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 3000L;
		dependentId = 4L;
		entity = ProductFactory.createProduct();
		entityCategory = CategoryFactory.createCategory();
		entityDTO = ProductFactory.createProductDTO();
		page = new PageImpl<>(List.of(entity));
		Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(entity);
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(entity));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(entity);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(entityCategory);
		Mockito.when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}

	@Test
	public void deleteShouldDoNotWhenIdExisting() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}

	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesExisting() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}

	@Test
	public void deleteShouldThrowEmptyResultDatabaseExceptionWhenIdHasADependent() {
		Assertions.assertThrows(DataBaseException.class, () -> {
			service.delete(dependentId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
	}

	/*
	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(pageable);
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}*/

	@Test
	public void findyByIdShouldReturnAProductDTOWhenIdExists() {
		ProductDTO entity = service.findById(existingId);
		Assertions.assertNotNull(entity);
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		ProductDTO result = service.update(existingId, entityDTO);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(entityDTO.getName(), result.getName());
		Mockito.verify(repository, Mockito.times(1)).getReferenceById(existingId);
	}

	@Test
	void updateShouldThrowEntityNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, entityDTO);
		});
		Mockito.verify(repository, Mockito.times(1)).getReferenceById(nonExistingId);
	}
}