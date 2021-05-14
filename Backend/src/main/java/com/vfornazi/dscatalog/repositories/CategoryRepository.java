package com.vfornazi.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vfornazi.dscatalog.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
	
}
