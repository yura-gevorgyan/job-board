package am.itspace.jobboard.controller.admin;

import am.itspace.jobboard.entity.Category;
import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.specification.CompanySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/companies")
public class AdminCompanyController {

    private final CompanyService companyService;

    private final CategoryService categoryService;

    @GetMapping("/{indexStr}")
    public String getCompaniesPage(@PathVariable(name = "indexStr") String indexStr, ModelMap modelMap) {
        try {
            if (indexStr == null || indexStr.isEmpty()) {
                return "redirect:/admin/companies/1";
            }
            int index = Integer.parseInt(indexStr);
            if (index <= 0) {
                return "redirect:/admin/companies/1";
            }

            Page<Company> companies = companyService.findAllCompanies(index);

            if (index > companies.getTotalPages() && companies.getTotalPages() != 0) {
                return "redirect:/admin/companies/1";
            }

            modelMap.put("page", "companies");

            modelMap.put("index", index);
            modelMap.put("searchIndex", 0);
            modelMap.put("totalPages", companies.getTotalPages());
            modelMap.put("companyCount", companies.getNumberOfElements());
            modelMap.put("categories", categoryService.findAll());
            modelMap.put("companies", companies);

        } catch (Exception e) {
            return "redirect:/admin/companies/1";
        }
        return "admin/admin-companies";
    }

    @GetMapping("/search")
    public String getAdminSearchJobPage(@RequestParam(value = "searchIndexStr", defaultValue = "1") String searchIndexStr,
                                        @RequestParam(value = "name", defaultValue = "") String name,
                                        @RequestParam(value = "category", defaultValue = "0") String categoryIdStr,
                                        ModelMap modelMap) {
        try {
            int categoryId = Integer.parseInt(categoryIdStr);
            int searchIndex = Integer.parseInt(searchIndexStr);
            if (searchIndex <= 0) {
                throw new Exception();
            }

            Category category = categoryService.findById(categoryId);
            if (category != null) {
                modelMap.put("category", category.getId());
            } else {
                modelMap.put("category", "");
            }

            Specification<Company> companySpecification = CompanySpecification.searchCompanies(name, category, null);
            Page<Company> companies = companyService.findAllCompanies(companySpecification, searchIndex);

            if (searchIndex > companies.getTotalPages() && companies.getTotalPages() != 0) {
                return "redirect:/admin/companies/1";
            }

            modelMap.put("page", "companies");

            modelMap.put("index", 0);
            modelMap.put("searchIndex", searchIndex);
            modelMap.put("name", name);
            modelMap.put("totalPages", companies.getTotalPages());
            modelMap.put("companyCount", companies.getNumberOfElements());
            modelMap.put("companies", companies);
            modelMap.put("categories", categoryService.findAll());

            return "admin/admin-companies";
        } catch (Exception e) {
            return "redirect:/admin/companies/1";
        }

    }


}
