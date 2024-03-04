package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.repository.CategoryRepository;
import am.itspace.jobboard.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

}
