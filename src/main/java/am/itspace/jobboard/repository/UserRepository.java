package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {


}
