package com.gustavonascimento.dscatalog.tests;

import java.time.Instant;

import com.gustavonascimento.dscatalog.entities.Category;
import com.gustavonascimento.dscatalog.entities.Product;
import com.gustavonascimento.dscatalog.entities.dto.ProductDTO;

public class ProductFactory {

	public static Product createProduct() {
		Product entity = new Product(1L, "Computer", "Super mega ultra blaster Computer", 10.0,
				"https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/4-big.jpg",
				Instant.parse("2024-01-13T10:00:00Z"));
		entity.getCategories().add(new Category(2L, "Eletronics"));
		return entity;
	}

	public static ProductDTO createProductDTO() {
		Product entity = createProduct();
		return new ProductDTO(entity, entity.getCategories());
	}
}