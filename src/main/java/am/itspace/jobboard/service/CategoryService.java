package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    boolean exists(int id);

    Category findById(int id);

    int getCategoryCount();

    boolean existsByName(String name);

    void save(String name, MultipartFile multipartFile);

    void update(int id, String name, MultipartFile multipartFile);

}
