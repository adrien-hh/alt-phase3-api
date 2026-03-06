package org.alt.altphase3api.repository;

import org.alt.altphase3api.domain.bo.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ToolRepository
    extends JpaRepository<Tool, Integer>, JpaSpecificationExecutor<Tool> {}
