package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.Category;
import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.CompanyWishlist;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.security.SecurityService;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.CompanyPictureService;
import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.service.CompanyWishlistService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.specification.CompanySpecification;
import am.itspace.jobboard.util.PictureUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final CategoryService categoryService;
    private final JobService jobService;
    private final CompanyPictureService companyPictureService;
    private final CompanyWishlistService companyWishlistService;
    private final SecurityService securityService;

    @GetMapping("/{index}")
    public String jobPage(@PathVariable("index") String indexStr, ModelMap modelMap) {
        User user = securityService.getCurrentUser();

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

            if (user != null) {
                List<CompanyWishlist> companyWishlists = companyWishlistService.findAllByUserId(user.getId());
                List<Company> companyList = new ArrayList<>();
                for (CompanyWishlist companyWishlist : companyWishlists) {
                    companyList.add(companyWishlist.getCompany());
                }
                modelMap.addAttribute("favoritesCompanies", companyList);
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
        User user = securityService.getCurrentUser();

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

            if (searchIndex > companies.getTotalPages() && companies.getTotalPages() != 0) {
                return "redirect:/companies/1";
            }

            addAttributes(modelMap, url, companies, searchIndex, 0);
            modelMap.addAttribute("currentName", name);
            modelMap.addAttribute("currentCategoryId", categoryId);

            if (user != null) {
                List<CompanyWishlist> companyWishlists = companyWishlistService.findAllByUserId(user.getId());
                List<Company> companyList = new ArrayList<>();
                for (CompanyWishlist companyWishlist : companyWishlists) {
                    companyList.add(companyWishlist.getCompany());
                }
                modelMap.addAttribute("favoritesCompanies", companyList);
            }
            return "company-list";
        } catch (IllegalArgumentException e) {
            return "redirect:/companies/1";
        }
    }

    @GetMapping("/item/{id}")
    public String getSingleCompanyPage(@PathVariable("id") String idStr, ModelMap modelMap) {
        User user = securityService.getCurrentUser();
        int id;
        try {
            id = Integer.parseInt(idStr);
            Company company = companyService.findById(id);
            if (company == null) {
                throw new NumberFormatException();
            }
            if (!company.isActive()) {
                if (user == null) {
                    throw new NumberFormatException();
                } else if (!user.getRole().equals(Role.ADMIN)) {
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

    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createCompany(@Valid @ModelAttribute Company company,
                                BindingResult bindingResult,
                                @RequestParam("categoryId") String categoryIdStr,
                                @RequestParam("logo") MultipartFile logo,
                                @RequestParam("companyPictures") MultipartFile[] companyPictures,
                                RedirectAttributes redirectAttributes) {
        User user = securityService.getCurrentUser();

        if (bindingResult.hasErrors()) {
            int errorCount = bindingResult.getErrorCount();
            if (errorCount > 1) {
                addFlashAttributes(redirectAttributes, "You should fill all the fields by right way.");
            } else if (errorCount == 1) {
                addFlashAttributes(redirectAttributes, bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            return "redirect:/profile/company";
        }
        if (logo.isEmpty() || logo.getSize() < 1) {
            addFlashAttributes(redirectAttributes, "Company logo is required.");
            return "redirect:/profile/company";

        } else if (!PictureUtil.isFileSizeValid(logo)) {
            addFlashAttributes(redirectAttributes, "The company logo must be a maximum of 10MB in size.");
            return "redirect:/profile/company";
        }

        try {
            int categoryId = Integer.parseInt(categoryIdStr);
            Category byId = categoryService.findById(categoryId);
            if (byId == null) {
                throw new Exception();
            }
            company.setCategory(byId);
        } catch (Exception e) {
            addFlashAttributes(redirectAttributes, "Wrong category id for company.");
        }

        companyService.create(company, user, logo);
        companyPictureService.addPictures(company, companyPictures);

        return "redirect:/profile/company";
    }

    @PostMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String updateCompany(@Valid @ModelAttribute Company company,
                                BindingResult bindingResult,
                                @RequestParam("categoryId") String categoryIdStr,
                                @RequestParam("logo") MultipartFile logo,
                                @RequestParam("companyPictures") MultipartFile[] companyPictures,
                                @RequestParam("deletedPictures") String[] deletedPictures,
                                RedirectAttributes redirectAttributes) {
        User user = securityService.getCurrentUser();

        if (bindingResult.hasErrors()) {
            int errorCount = bindingResult.getErrorCount();
            if (errorCount > 1) {
                addFlashAttributes(redirectAttributes, "You should fill all the fields by right way.");
            } else if (errorCount == 1) {
                addFlashAttributes(redirectAttributes, bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            return "redirect:/profile/company";
        }

        Company userOldCompany = companyService.findById(company.getId());
        if (userOldCompany == null || !userOldCompany.getUser().equals(user)) {
            addFlashAttributes(redirectAttributes, "Wrong company data.");
            return "redirect:/profile/company";
        }
        try {
            int categoryId = Integer.parseInt(categoryIdStr);
            Category byId = categoryService.findById(categoryId);
            if (byId == null) {
                throw new Exception();
            }
            company.setCategory(byId);
        } catch (Exception e) {
            addFlashAttributes(redirectAttributes, "Wrong category id for company.");
        }

        Company updated = companyService.update(userOldCompany, company, user, logo);
        companyPictureService.update(updated, companyPictures, deletedPictures);

        return "redirect:/profile/company";
    }

    @GetMapping("/favorites/{index}")
    public String favoritesJobs(@PathVariable("index") String indexStr, ModelMap modelMap) {
        User user = securityService.getCurrentUser();

        if (user != null) {
            try {
                if (indexStr == null || indexStr.isEmpty()) {
                    return "redirect:/companies/favorites/1";
                }

                int index = Integer.parseInt(indexStr);

                if (index <= 0) {
                    return "redirect:/companies/favorites/1";
                }

                Page<CompanyWishlist> byUserid = companyWishlistService.findByUserId(index, user.getId());

                if (index > byUserid.getTotalPages() && byUserid.getTotalPages() != 0) {
                    return "redirect:/companies/favorites/1";
                }

                modelMap.addAttribute("favoritesCompanies", byUserid);
                modelMap.addAttribute("index", index);
                modelMap.addAttribute("totalPages", byUserid.getTotalPages());

                return "favorites-companies";

            } catch (NumberFormatException e) {
                return "redirect:/companies/favorites/1";
            }

        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/favorites/add/{idStr}")
    public ResponseEntity<?> addWishlist(@PathVariable("idStr") String idStr) {
        User user = securityService.getCurrentUser();

        if (user != null) {
            try {
                int id = Integer.parseInt(idStr);

                Company company = companyService.findById(id);

                if (company != null && company.isActive()) {
                    companyWishlistService.save(company, user);
                    return ResponseEntity.ok().build();
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/favorites/delete/{idStr}")
    public ResponseEntity<?> deleteWishlist(@PathVariable("idStr") String idStr) {
        User user = securityService.getCurrentUser();

        if (user != null) {
            try {
                int id = Integer.parseInt(idStr);

                Company company = companyService.findById(id);

                if (company != null && company.isActive()) {
                    companyWishlistService.delete(company, user);
                    return ResponseEntity.ok().build();
                }

            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    private void addFlashAttributes(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("msg", message);
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
