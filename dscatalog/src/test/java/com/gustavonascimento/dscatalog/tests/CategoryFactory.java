package com.gustavonascimento.dscatalog.tests;

import com.gustavonascimento.dscatalog.entities.Category;

public class CategoryFactory {

	public static Category createCategory() {
		return new Category(1L, "Electronics");
	}
}