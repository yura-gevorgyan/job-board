package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.security.SpringUser;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.CompanyPictureService;
import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.specification.CompanySpecification;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/companies")

public class CompanyController {

    private final CompanyService companyService;

    private final CategoryService categoryService;

    private final JobService jobService;

    private final CompanyPictureService companyPictureService;

    @GetMapping("/{index}")
    public String jobPage(@PathVariable("index") String indexStr, ModelMap modelMap) {
        try {
            if (indexStr == null || indexStr.isEmpty()) {
                return "redirect:/companies/1";
            }
            int index = Integer.parseInt(indexStr);
            if (index <= 0) {
                return "redirect:/companies/1";
            }

            Page<Company> companies = companyService.findAllByIsActiveTrue(index);

            if (index > companies.getTotalPages() && companies.getTotalPages() != 0) {
                return "redirect:/companies/1";
            }

            addAttributes(modelMap, null, companies, 0, index);

            return "company-list";
        } catch (NumberFormatException e) {
            return "redirect:/companies/1";
        }
    }

    @GetMapping("/search")
    public String jobSearch(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "category", required = false, defaultValue = "0") String categoryIdStr,
                            @RequestParam(value = "searchIndexStr", required = false) String searchIndexStr,
                            HttpServletRequest httpServletRequest,
                            ModelMap modelMap) {
        try {
            int searchIndex = Integer.parseInt(searchIndexStr);
            int categoryId = Integer.parseInt(categoryIdStr);
            String string = httpServletRequest.getQueryString();
            int length = string.length() - 1;
            String url = string.substring(0, length);

            if (searchIndex <= 0 || searchIndex > Short.MAX_VALUE) {
                return "redirect:/companies/1";
            }

            Specification<Company> companySpecification = CompanySpecification.searchCompanies(name, categoryService.findById(categoryId), true);
            Page<Company> companies = companyService.findAll(companySpecification, searchIndex);

            if (searchIndex > companies.getTotalPages()) {
                return "redirect:/companies/1";
            }

            addAttributes(modelMap, url, companies, searchIndex, 0);
            modelMap.addAttribute("currentName", name);
            modelMap.addAttribute("currentCategoryId", categoryId);
            return "company-list";
        } catch (IllegalArgumentException e) {
            return "redirect:/companies/1";
        }
    }

    @GetMapping("/item/{id}")
    public String getSingleCompanyPage(@AuthenticationPrincipal SpringUser springUser, @PathVariable("id") String idStr, ModelMap modelMap) {
        int id;
        try {
            id = Integer.parseInt(idStr);
            Company company = companyService.findById(id);
            if (company == null) {
                throw new NumberFormatException();
            }
            if (!company.isActive()) {
                if (springUser == null) {
                    throw new NumberFormatException();
                } else if (!springUser.getUser().getRole().equals(Role.ADMIN)) {
                    throw new NumberFormatException();
                }
            }
            company.setDescription(company.getDescription().replace("\n", "<br>"));
            company.setActiveJobs(jobService.getCountByCompanyId(company.getId()));
            modelMap.put("company", company);
            modelMap.put("pictures", companyPictureService.findAllByCompanyId(company.getId()));
            modelMap.put("jobs", jobService.findTop8ByCompanyId(company.getId()));
            return "company-single";
        } catch (NumberFormatException e) {
            return "redirect:/";
        }
    }

    private void addAttributes(ModelMap modelMap, String url, Page<Company> companies, int searchIndex, int index) {
        modelMap.addAttribute("url", url);
        modelMap.addAttribute("companies", companies);
        modelMap.addAttribute("searchIndex", searchIndex);
        modelMap.addAttribute("index", index);
        modelMap.addAttribute("totalPages", companies.getTotalPages());
        modelMap.addAttribute("companyCount", companies.getTotalElements());
        modelMap.addAttribute("categories", categoryService.findAll());
    }
}



