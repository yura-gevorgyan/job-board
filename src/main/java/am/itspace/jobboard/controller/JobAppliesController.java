package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.JobApplies;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.ApplicantListStatus;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.security.SpringUser;
import am.itspace.jobboard.service.JobAppliesService;
import am.itspace.jobboard.service.ResumeService;
import am.itspace.jobboard.specification.JobAppliesSpecification;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@RequestMapping("/job/applies")
public class JobAppliesController {

    private final JobAppliesService jobAppliesService;
    private final ResumeService resumeService;

    @GetMapping("/{index}")
    public String jobApplies(@PathVariable("index") String indexStr,
                             @AuthenticationPrincipal SpringUser springUser,
                             ModelMap modelMap) {

        User user = springUser.getUser();
        if (user != null && user.getRole() == Role.JOB_SEEKER) {
            if (indexStr == null || indexStr.isEmpty()) {
                return "redirect:/job/applies/1";
            }

            int index = Integer.parseInt(indexStr);

            if (resumeService.findByUserIdAndIsActiveTrue(user.getId()) == null) {
                modelMap.addAttribute("createResumeMsg", "To view applied job history, you need to create a resume first.");
                return "/profile/candidate-applied-job";
            }

            if (index <= 0) {
                return "redirect:/job/applies/1";
            }

            Page<JobApplies> jobApplies = jobAppliesService.findAllByToJobSeekerIdAndIsActiveTrue(user.getId(), index);

            if (jobApplies == null || jobApplies.isEmpty()) {
                return "/profile/candidate-applied-job";
            }

            if (index > jobApplies.getTotalPages() && jobApplies.getTotalPages() != 0) {
                return "redirect:/job/applies/1";
            }

            addAttributes(modelMap, null, jobApplies, 0, index);
            return "/profile/candidate-shortlisted-jobs";
        }
        return "redirect:/";
    }


    @GetMapping("/search")
    public String search(@RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "date", required = false) String sendDate,
                         @RequestParam(value = "searchIndexStr", required = false) String searchIndexStr,
                         @AuthenticationPrincipal SpringUser springUser,
                         ModelMap modelMap,
                         HttpServletRequest httpServletRequest) {

        User user = springUser.getUser();
        if (user != null && user.getRole() == Role.JOB_SEEKER) {

            int searchIndex = Integer.parseInt(searchIndexStr);

            String string = httpServletRequest.getQueryString();
            int length = string.length() - 1;
            String url = string.substring(0, length);

            if (searchIndex <= 0 || searchIndex > Short.MAX_VALUE) {
                return "redirect:/job/applies/1";
            }

            Specification<JobApplies> jobAppliesSpecification = JobAppliesSpecification.filterByStatusAndLastDate(status, sendDate, user.getId(), true);
            Page<JobApplies> jobApplies = jobAppliesService.findAllByToJobSeekerIdAndIsActiveTrue(jobAppliesSpecification, searchIndex);

            if (searchIndex > jobApplies.getTotalPages() && jobApplies.getTotalPages() != 0) {
                return "redirect:/job/applies/1";
            }

            addAttributes(modelMap, url, jobApplies, searchIndex, 0);
            modelMap.addAttribute("currentStatus", status);
            modelMap.addAttribute("currentDate", sendDate);
            return "/profile/candidate-shortlisted-jobs";
        }
        return "redirect:/";
    }

    @GetMapping("/approve/{id}")
    public String approveJobApply(@PathVariable("id") String idStr) {
        int id = Integer.parseInt(idStr);
        jobAppliesService.changeStatusApproved(id);
        return "redirect:/job/applies/1";
    }

    @GetMapping("/reject/{id}")
    public String rejectJobApply(@PathVariable("id") String idStr) {
        int id = Integer.parseInt(idStr);
        jobAppliesService.changeStatusRejected(id);
        return "redirect:/job/applies/1";
    }

    private void addAttributes(ModelMap modelMap, String url, Page<JobApplies> jobApplies, int searchIndex, int index) {
        modelMap.addAttribute("url", url);
        modelMap.addAttribute("jobApplies", jobApplies);
        modelMap.addAttribute("searchIndex", searchIndex);
        modelMap.addAttribute("index", index);
        modelMap.addAttribute("totalPages", jobApplies.getTotalPages());
        modelMap.addAttribute("jobAppliesCount", jobApplies.getTotalElements());
        modelMap.addAttribute("statuses", ApplicantListStatus.values());
    }
}
