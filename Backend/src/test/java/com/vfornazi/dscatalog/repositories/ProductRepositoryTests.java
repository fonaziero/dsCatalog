package com.vfornazi.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.vfornazi.dscatalog.entity.Product;
import com.vfornazi.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	
	private long noIdProduct;
	
	@BeforeEach
	void setup() throws Exception {
		noIdProduct = 0L;
	}
	
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(26, product.getId());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(1L);
		
		Optional<Product> result = repository.findById(1L);
		
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			
			repository.deleteById(noIdProduct);
			
		});
	}

	@Test
	public void findByIdShouldReturnObjectWhenIdExists() {
		
		Optional<Product> result = repository.findById(1L);
		
		Assertions.assertTrue(result.isPresent());
		
	}
	
	@Test
	public void findByIdShouldReturnEmpytObjectWhenIdNotExists() {
		
		Optional<Product> result = repository.findById(0L);
		
		Assertions.assertTrue(result.isEmpty());
	}
	
	
	
}
