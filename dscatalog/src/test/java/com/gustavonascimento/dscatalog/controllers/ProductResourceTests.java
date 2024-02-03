package com.gustavonascimento.dscatalog.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavonascimento.dscatalog.entities.dto.ProductDTO;
import com.gustavonascimento.dscatalog.services.ProductService;
import com.gustavonascimento.dscatalog.services.exceptions.DataBaseException;
import com.gustavonascimento.dscatalog.services.exceptions.ResourceNotFoundException;
import com.gustavonascimento.dscatalog.tests.ProductFactory;

@WebMvcTest(ProductController.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private PageImpl<ProductDTO> page;
	private ProductDTO entityDto;
	private Long existsId;
	private Long nonExistsId;
	private Long dependentId;

	@MockBean
	private ProductService service;

	@BeforeEach
	void setUp() throws Exception {
		entityDto = ProductFactory.createProductDTO();
		page = new PageImpl<>(List.of(entityDto));
		existsId = 1L;
		nonExistsId = 999L;
		dependentId = 3L;
		Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(service.findById(existsId)).thenReturn(entityDto);
		Mockito.when(service.findById(nonExistsId)).thenThrow(ResourceNotFoundException.class);
		Mockito.when(service.update(eq(existsId), any())).thenReturn(entityDto);
		Mockito.when(service.update(eq(nonExistsId), any())).thenThrow(ResourceNotFoundException.class);
		Mockito.doNothing().when(service).delete(existsId);
		Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistsId);
		Mockito.doThrow(DataBaseException.class).when(service).delete(dependentId);
		Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(entityDto);
	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions results = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
		results.andExpect(status().isOk());
	}

	@Test
	public void findyByIdShouldReturnProductDTOWhenIdExists() throws Exception {
		ResultActions results = mockMvc.perform(get("/products/{id}", existsId).accept(MediaType.APPLICATION_JSON));
		results.andExpect(status().isOk());
		results.andExpect(jsonPath("$.id").exists());
		results.andExpect(jsonPath("$.name").exists());
	}

	@Test
	public void findyByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions results = mockMvc.perform(get("/products/{id}", nonExistsId).accept(MediaType.APPLICATION_JSON));
		results.andExpect(status().isNotFound());
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(entityDto);
		ResultActions results = mockMvc.perform(put("/products/{id}", existsId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		results.andExpect(status().isOk());
		results.andExpect(jsonPath("$.name").exists());
		results.andExpect(jsonPath("$.description").exists());
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(entityDto);
		ResultActions results = mockMvc.perform(put("/products/{id}", nonExistsId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		results.andExpect(status().isNotFound());
		results.andExpect(jsonPath("$.name").doesNotExist());
	}

	@Test
	public void insertShouldReturnCreatedAndProductDTO() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(entityDto);
		ResultActions results = mockMvc.perform(post("/products").content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		results.andExpect(status().isCreated());
		results.andExpect(jsonPath("$.name").exists());
		results.andExpect(jsonPath("$.description").exists());
	}

	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		ResultActions results = mockMvc.perform(delete("/products/{id}", existsId).accept(MediaType.APPLICATION_JSON));
		results.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShoudThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {
		ResultActions results = mockMvc.perform(delete("/products/{id}", nonExistsId).accept(MediaType.APPLICATION_JSON));
		results.andExpect(status().isNotFound());
	}
}