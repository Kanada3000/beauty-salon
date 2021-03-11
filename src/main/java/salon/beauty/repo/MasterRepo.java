package salon.beauty.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import salon.beauty.domain.Category;
import salon.beauty.domain.Master;
import salon.beauty.domain.User;

import java.util.List;
import java.util.Set;

public interface MasterRepo extends JpaRepository<Master, Long> {

    @Query("SELECT m FROM Master m WHERE m.id = :id")
    Master findMasterById(@Param("id") Long id);

    List<Master> findByUsers(User user);

    @Query("SELECT m FROM Master m WHERE m.category =?1")
    List<Master> findMasterByCategory(Category category);

    @Query("SELECT m FROM Master m WHERE m.users.id=?1 AND m.category=?2 AND m.time=?3")
    Master findByUserId(Long id, Category category, String time);

    @Query("SELECT m.category FROM Master m WHERE m IN ?1")
    Set<Category> findAllCategory(List<Master> master);

}