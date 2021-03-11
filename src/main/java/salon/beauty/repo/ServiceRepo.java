package salon.beauty.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import salon.beauty.domain.Category;
import salon.beauty.domain.Services;

import java.util.List;
import java.util.Set;

public interface ServiceRepo extends JpaRepository<Services, Long> {
    List<Services> findByCategory(Category category);

    @Query("SELECT s FROM Services s Where s.category IN ?1")
    List<Services> findByCategories(Set<Category> category);

    @Query("SELECT s FROM Services s WHERE s.id = :id")
    Services findServicesById(@Param("id") Long id);
}