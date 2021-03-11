package salon.beauty.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import salon.beauty.domain.Role;
import salon.beauty.domain.Services;
import salon.beauty.domain.User;

import java.util.List;
import java.util.Set;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT u FROM User u join u.roles ur WHERE ur =?1")
    List<User> findAllByRoleUser(Set<Role> roles);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    User findUserById(@Param("id") Long id);

}