package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Category;
import am.itspace.jobboard.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    int countBy();

    boolean existsByName(String name);

    @Query(value = "SELECT * FROM category ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Category> findRandomCategories(int limit);

}

