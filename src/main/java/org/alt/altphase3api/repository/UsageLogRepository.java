package org.alt.altphase3api.repository;

import java.time.LocalDate;
import org.alt.altphase3api.domain.bo.UsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsageLogRepository extends JpaRepository<UsageLog, Integer> {
  long countByToolIdAndSessionDateAfter(Integer toolId, LocalDate sessionDateAfter);

  @Query(
      """
        SELECT COALESCE(AVG(u.usageMinutes), 0)
        from UsageLog u
        where u.tool.id = :toolId
          and u.sessionDate >= :sessionDateAfter
    """)
  Double findAverageUsageMinutes(Integer toolId, LocalDate sessionDateAfter);
}
