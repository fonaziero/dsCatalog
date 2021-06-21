package com.vfornazi.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vfornazi.dscatalog.dto.ProductDTO;
import com.vfornazi.dscatalog.services.ProductService;
import com.vfornazi.dscatalog.services.exceptions.DataBaseException;
import com.vfornazi.dscatalog.services.exceptions.ResourceNotFoundException;
import com.vfornazi.dscatalog.tests.Factory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private ProductDTO dto;
	private PageImpl<ProductDTO> page;
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private String jsonBody;
	
	@BeforeEach
	void setUp() throws Exception {
		
		existingId = 1L;
		nonExistingId = 0L;
		dependentId = 3L;
		
		dto = Factory.createProductDTO();
		jsonBody = objectMapper.writeValueAsString(dto); 
		page = new PageImpl<>(List.of(dto));
		
		//FindAllPageable
		when(service.findAllPaged(any())).thenReturn(page);
		
		//FindById
		when(service.findById(existingId)).thenReturn(dto);
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		//update
		when(service.update(eq(existingId), any())).thenReturn(dto);
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		//delete
		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DataBaseException.class).when(service).delete(dependentId);
	
		//insert
		when(service.insert(any())).thenReturn(dto);
	}
	
	@Test
	public void insertShouldCreateProductDTO() throws Exception {
		
		ResultActions result =  mockMvc.perform(post("/products")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		
		ResultActions result =  mockMvc.perform(delete("/products/{id}", existingId)
		.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShouldReturnNoTFoundWhenIdDoesNotExists() throws Exception {
		
		ResultActions result =  mockMvc.perform(delete("/products/{id}", nonExistingId)
		.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		
		ResultActions result =  mockMvc.perform(put("/products/{id}", existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		
		ResultActions result =  mockMvc.perform(put("/products/{id}", nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	
	@Test
	public void findAllShouldReturnPageable () throws Exception {
		
		ResultActions result =  mockMvc.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		
	}
	
	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception {
		
		ResultActions result =  mockMvc.perform(get("/products/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdNonExists() throws Exception {
		
		ResultActions result =  mockMvc.perform(get("/products/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
		
	}
	
}
