package org.alt.altphase3api.repository.spec;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.alt.altphase3api.domain.bo.Tool;
import org.alt.altphase3api.domain.enums.Department;
import org.alt.altphase3api.domain.enums.ToolStatus;
import org.alt.altphase3api.dto.ToolSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class ToolSpecifications {

    public static Specification<Tool> withFilters(ToolSearchCriteria c) {
        return Specification.where(hasDepartment(c.department()))
                .and(hasStatus(c.status()))
                .and(hasCategory(c.category()))
                .and(hasMinCost(c.minCost()))
                .and(hasMaxCost(c.maxCost()));
    }

    private static Specification<Tool> hasDepartment(Department department) {
        return (root, query, cb) ->
                department == null ? null :
                        cb.equal(root.get("ownerDepartment"), department);
    }

    private static Specification<Tool> hasStatus(ToolStatus status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("status"), status);
    }

    private static Specification<Tool> hasCategory(String category) {
        return (root, query, cb) ->
                (category == null || category.isBlank()) ? null :
                        cb.equal(root.join("category").get("name"), category);
    }

    private static Specification<Tool> hasMinCost(BigDecimal minCost) {
        return (root, query, cb) ->
                minCost == null ? null :
                        cb.greaterThanOrEqualTo(root.get("monthlyCost"), minCost);
    }

    private static Specification<Tool> hasMaxCost(BigDecimal maxCost) {
        return (root, query, cb) ->
                maxCost == null ? null :
                        cb.lessThanOrEqualTo(root.get("monthlyCost"), maxCost);
    }
}