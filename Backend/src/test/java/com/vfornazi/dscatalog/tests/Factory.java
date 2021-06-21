package com.vfornazi.dscatalog.tests;

import java.time.Instant;

import com.vfornazi.dscatalog.dto.ProductDTO;
import com.vfornazi.dscatalog.entity.Category;
import com.vfornazi.dscatalog.entity.Product;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
		product.getCategoreis().add(creatyCategory());
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		
		Product product = createProduct();
		
		return new ProductDTO(product, product.getCategoreis());
	}
	
	public static Category creatyCategory() {
		return new Category(2L, "Eletronics");
	}
	
}
