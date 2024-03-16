package am.itspace.jobboard.controller.admin;

import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.CompanyService;
import lombok.RequiredArgsConstructor;
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
        int totalPages = companyService.getTotalPages();
        int index;
        try {
            index = Integer.parseInt(indexStr);
            if (index <= 0 || (index > totalPages && totalPages != 0)) {
                return "redirect:/admin/companies/1";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/companies/1";
        }

        modelMap.put("page", "companies");

        modelMap.put("index", index);
        modelMap.put("searchIndex", 0);
        modelMap.put("totalPages", totalPages);
        modelMap.put("categories", categoryService.findAll());
        modelMap.put("companyCount", companyService.getCompanyCount());
        modelMap.put("companies", companyService.getCompaniesFromNToM(index));
        return "admin/admin-companies";
    }

    @GetMapping("/search")
    public String getAdminSearchJobPage(@RequestParam(value = "searchIndexStr", defaultValue = "1") String searchIndexStr,
                                        @RequestParam(value = "name") String name,
                                        @RequestParam(value = "category", defaultValue = "") String categoryIdStr, ModelMap modelMap) {
        int categoryId;
        try {
            categoryId = Integer.parseInt(categoryIdStr);
        } catch (NumberFormatException e) {
            categoryId = 0;
        }
        int totalPagesOfSearchCategoryName = companyService.getTotalPagesOfSearch(categoryId, name);
        int searchIndex;

        modelMap.put("page", "companies");

        try {
            searchIndex = Integer.parseInt(searchIndexStr);
            if (searchIndex <= 0 || searchIndex > totalPagesOfSearchCategoryName) {
                return "redirect:/admin/companies/1";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/companies/1";
        }

        modelMap.put("index", 0);
        modelMap.put("searchIndex", searchIndex);
        modelMap.put("name", name);
        if (categoryService.exists(categoryId)) {
            modelMap.put("category", categoryId);
        } else {
            modelMap.put("category", "");
        }
        modelMap.put("totalPages", totalPagesOfSearchCategoryName);
        modelMap.put("categories", categoryService.findAll());
        modelMap.put("companyCount", companyService.getCompanyCountOfCategoryName(categoryId, name));
        modelMap.put("companies", companyService.getCompaniesFromNToMForSearch(searchIndex, categoryId, name));
        return "admin/admin-companies";
    }

}
