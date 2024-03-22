package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    int countBy();

    boolean existsByName(String name);

    List<Category> findTop9ByOrderByPicName();

}

