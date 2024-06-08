package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Category;
import am.itspace.jobboard.repository.CategoryRepository;
import lombok.Value;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryServiceImpl categoryService;


    @Test
    void findAll() {
        List<Category> categories = Arrays.asList(
                new Category(1, "Category 1", "pic1.jpg"),
                new Category(2, "Category 2", "pic2.jpg")
        );
        when(categoryRepository.findAll()).thenReturn(categories);
        List<Category> result = categoryService.findAll();
        assertEquals(categories.size(), result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void exists() {
        int categoryId = 1;
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        boolean result = categoryService.exists(categoryId);
        assertTrue(result);
        verify(categoryRepository, times(1)).existsById(categoryId);
    }

    @Test
    void findById() {
        int categoryId = 1;
        Category category = new Category(categoryId, "Category 1", "pic1.jpg");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Category result = categoryService.findById(categoryId);
        assertEquals(category, result);
        verify(categoryRepository, times(1)).findById(categoryId);
    }
}
