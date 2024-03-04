package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {


}
