package am.itspace.jobboard.controller.advice;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.security.SpringUser;
import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class UserRoleControllerAdvice {

    private final CompanyService companyService;
    private final ResumeService resumeService;
    private final JobService jobService;

    @ModelAttribute("hasCompany")
    public Company hasCompany(@AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null && springUser.getUser().getRole() == Role.COMPANY_OWNER) {
            return companyService.findCompanyByUserId(springUser.getUser().getId()) ;
        }
        return null;
    }

    @ModelAttribute("hasResume")
    public Resume hasResume(@AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null && springUser.getUser().getRole() == Role.JOB_SEEKER) {
            return resumeService.findByUserId(springUser.getUser().getId());
        }
        return null;
    }

    @ModelAttribute("hasJob")
    public Job hasjob(@AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null && springUser.getUser().getRole() == Role.EMPLOYEE) {
            return jobService.findByUserId(springUser.getUser().getId());
        }
        return null;
    }
}
