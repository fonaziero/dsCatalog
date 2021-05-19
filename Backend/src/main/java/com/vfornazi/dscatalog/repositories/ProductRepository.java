package com.vfornazi.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vfornazi.dscatalog.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	
}
