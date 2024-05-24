package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobWishlist;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.JobWishlistService;
import am.itspace.jobboard.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final CategoryService categoryService;
    private final JobService jobService;
    private final CompanyService companyService;

    @Override
    public void showHomePageDetails(ModelMap modelMap) {

        modelMap.addAttribute("firstCategories", categoryService.findTop(6));
        modelMap.addAttribute("categories", categoryService.findTop(9));
        modelMap.addAttribute("jobs", jobService.findTop6());

        List<Company> randomCompanies = companyService.findRandomCompanies(6);
        Map<Company, Integer> companyIntegerHashMap = randomCompanies.stream()
                .collect(Collectors.toMap(
                        company -> company,
                        company -> jobService.getCountByCompanyId(company.getId())
                ));
        modelMap.addAttribute("companies", companyIntegerHashMap);
    }
}
