package com.vfornazi.dscatalog.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

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

import com.vfornazi.dscatalog.dto.ProductDTO;
import com.vfornazi.dscatalog.entity.Category;
import com.vfornazi.dscatalog.entity.Product;
import com.vfornazi.dscatalog.repositories.CategoryRepository;
import com.vfornazi.dscatalog.repositories.ProductRepository;
import com.vfornazi.dscatalog.services.exceptions.DataBaseException;
import com.vfornazi.dscatalog.services.exceptions.ResourceNotFoundException;
import com.vfornazi.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private long existingId;
	private long noExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	private ProductDTO dto;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		dependentId = 2L;
		noExistingId = 0L;
		product = Factory.createProduct();
		category = Factory.creatyCategory();
		page = new PageImpl<>(List.of(product));
		dto = Factory.createProductDTO();
		
		
		// FindAll<Pageable>
		Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
		
		
		//FindById
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(noExistingId)).thenReturn(Optional.empty());
		
		//update
		Mockito.when(repository.getOne(existingId)).thenReturn(product);
		Mockito.when(repository.getOne(noExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
		Mockito.when(categoryRepository.getOne(noExistingId)).thenThrow(EntityNotFoundException.class);
		
		//Save
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		
		// Delete
		doNothing().when(repository).deleteById(existingId);
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(noExistingId);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	
	@Test
	public void findAllPageShouldReturnPage() {
		
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}
	
	
	@Test
	public void findByIdShouldReturnProductWhenIdExists() {
		
		ProductDTO result = service.findById(existingId);
		
		Assertions.assertNotNull(result);
		
		verify(repository, times(1)).findById(existingId);
	}
	
	
	@Test
	public void findByIdShouldThrowExceptionWhenIdNotExists() {		
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			
			service.findById(noExistingId);
			
		});
	}
	
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		
		ProductDTO result = service.update(existingId, dto);
		
		Assertions.assertNotNull(result);
	}
	
	
	@Test
	public void updateShouldThrowExceptionWhenIdNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			
			service.update(noExistingId, dto);

		});
		
	}
	
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		
		verify(repository, times(1)).deleteById(existingId);
		
	}
	
	
	@Test
	public void deleteShouldThrowNotFoundExcepionWhenIdNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			
			service.delete(noExistingId);
			
		});
		verify(repository, times(1)).deleteById(noExistingId);
	}
	
	
	@Test
	public void deleteShouldThrowDataBaseExcepionWhenIdIsDiferent() {
		
		Assertions.assertThrows(DataBaseException.class, () -> {
			
			service.delete(dependentId);
			
		});
		verify(repository, times(1)).deleteById(dependentId);
	}
	
	
}
