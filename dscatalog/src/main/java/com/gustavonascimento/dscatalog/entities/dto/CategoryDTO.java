package com.gustavonascimento.dscatalog.entities.dto;

import java.io.Serializable;

import com.gustavonascimento.dscatalog.entities.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	@Schema(description = "Categorie name")
	@NotBlank(message = "O campo nome é obrigatório")
	@Size(min = 6, max = 60, message = "O campo nome deve conter entre 6 e 60 caracteres.")
	private String name;

	public CategoryDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public CategoryDTO(Category entity) {
		this.id = entity.getId();
		this.name = entity.getName();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}