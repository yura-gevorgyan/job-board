package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Category;
import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.service.JobService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainServiceImplTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private JobService jobService;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private MainServiceImpl mainService;

    @Test
    void testShowHomePageDetails() {
        ModelMap modelMap = new ModelMap();
        List<Category> topCategories = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        List<Job> jobs = new ArrayList<>();
        List<Company> randomCompanies = new ArrayList<>();
        Map<Company, Integer> companyJobCountMap = new HashMap<>();

        when(categoryService.findTop(6)).thenReturn(topCategories);
        when(categoryService.findTop(9)).thenReturn(categories);

        when(jobService.findTop6()).thenReturn(jobs);

        when(companyService.findRandomCompanies(6)).thenReturn(randomCompanies);
        for (Company company : randomCompanies) {
            int jobCount = 10;
            companyJobCountMap.put(company, jobCount);
            when(jobService.getCountByCompanyId(company.getId())).thenReturn(jobCount);
        }

        mainService.showHomePageDetails(modelMap);

        assertEquals(topCategories, modelMap.get("firstCategories"));
        assertEquals(categories, modelMap.get("categories"));
        assertEquals(jobs, modelMap.get("jobs"));
        assertEquals(companyJobCountMap, modelMap.get("companies"));
    }
}