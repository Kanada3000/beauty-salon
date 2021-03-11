package salon.beauty.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import salon.beauty.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface VisitRepo extends JpaRepository<Visit, Long> {


    List<Visit> findByUsers(User users);

    @Query("select v.date from Visit v")
    Set<LocalDate> findAllDate();

    @Query("Select v from Visit v where v.status=?1")
    List<Visit> findByStatus(Status status);

    @Query("Select v from Visit v where v.status = ?3 and v.date = ?1 and v.time = ?2")
    List<Visit> findTime(LocalDate date, String time, Status status);

    Visit findFirst1ByOrderByIdDesc();

    @Query("select v from Visit v where v.master IN ?1")
    List<Visit> findByMaster(List<Master> master);

    @Query("Select v from Visit v where v.id = ?1")
    Visit findVisitById(Long id);
}