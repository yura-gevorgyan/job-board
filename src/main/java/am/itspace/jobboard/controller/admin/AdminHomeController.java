package am.itspace.jobboard.controller.admin;

import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.ResumeService;
import am.itspace.jobboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminHomeController {

    private final UserService userService;

    private final JobService jobService;

    private final ResumeService resumeService;

    private final CompanyService companyService;

    @GetMapping
    public String getAdminHomePage(ModelMap modelMap) {
        modelMap.put("page", "dashboard");

        modelMap.put("userCount", userService.getUserCount());
        modelMap.put("jobCount", jobService.getJobCount());
        modelMap.put("resumeCount", resumeService.getResumeCount());
        modelMap.put("companyCount", companyService.getCompanyCount());
        modelMap.put("jobs", jobService.getLast4Jobs());
        modelMap.put("resumes", resumeService.getLast6Resumes());
        modelMap.put("users", userService.getLast4Users());
        return "admin/admin-dashboard";
    }

}
