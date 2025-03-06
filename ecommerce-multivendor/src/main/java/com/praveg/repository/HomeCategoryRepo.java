package com.praveg.repository;

import com.praveg.entity.HomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeCategoryRepo extends JpaRepository<HomeCategory, Long> {
}
