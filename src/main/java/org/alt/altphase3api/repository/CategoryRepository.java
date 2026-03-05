package org.alt.altphase3api.repository;

import org.alt.altphase3api.domain.bo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {}
