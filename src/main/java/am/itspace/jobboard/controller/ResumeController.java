package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.enums.Gender;
import am.itspace.jobboard.entity.enums.WorkExperience;
import am.itspace.jobboard.security.SpringUser;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.ResumeService;
import am.itspace.jobboard.util.AddMessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resume")
public class ResumeController {

    private final ResumeService resumeService;
    private final CategoryService categoryService;

    @GetMapping("/profile")
    public String createCompanyPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap,
                                    @AuthenticationPrincipal SpringUser springUser) {
        AddMessageUtil.addMessageToModel(msg, modelMap);
        if (springUser != null) {
            modelMap.addAttribute("categories", categoryService.findAll());
            modelMap.addAttribute("gender", Gender.values());
            modelMap.addAttribute("workExperience", WorkExperience.values());
            modelMap.addAttribute("resume", resumeService.findByUserId(springUser.getUser().getId()));
            return "/profile/candidate-profile";
        }
        return "/profile/candidate-profile";
    }

    @GetMapping("/applied/jobs")
    public String companySinglePage() {
        return "/profile/candidate-applied-job";
    }
}
