package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Category;
import am.itspace.jobboard.repository.CategoryRepository;
import am.itspace.jobboard.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    @Value("${program.pictures.file.path}")
    private String FILE_PATH;


    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public boolean exists(int id) {
        return categoryRepository.existsById(id);
    }

    @Override
    public Category findById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public int getCategoryCount() {
        return categoryRepository.countBy();
    }

    @Override
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    public void save(String name, MultipartFile multipartFile) {
        String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        File file = new File(FILE_PATH + File.separator + fileName);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        categoryRepository.save(Category.builder()
                .name(name)
                .picName(fileName)
                .build());
    }

    @Override
    public void update(int id, String name, MultipartFile multipartFile) {
        Category category;
        Optional<Category> byId = categoryRepository.findById(id);
        if (byId.isPresent()) {
            category = byId.get();
            if (multipartFile != null && !multipartFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                File file = new File(FILE_PATH + File.separator + fileName);
                try {
                    multipartFile.transferTo(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                category.setPicName(fileName);
            }
            category.setName(name);
            categoryRepository.save(category);
        }
    }

    @Override
    public List<Category> findTop(int topNumber) {
        return categoryRepository.findRandomCategories(topNumber);
    }

}

