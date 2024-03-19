package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.entity.enums.WorkExperience;
import am.itspace.jobboard.security.SpringUser;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.util.AddMessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;
    private final CategoryService categoryService;

    @GetMapping("/profile")
    public String companyProfilePage(
            @RequestParam(value = "msg", required = false) String msg, ModelMap modelMap,
            @AuthenticationPrincipal SpringUser springUser) {
        AddMessageUtil.addMessageToModel(msg, modelMap);
        if (springUser != null) {
            modelMap.addAttribute("categories", categoryService.findAll());
            modelMap.addAttribute("company", companyService.findCompanyByUserId(springUser.getUser().getId()));
            return "company-profile";
        }
        return "company-profile";
    }
}
