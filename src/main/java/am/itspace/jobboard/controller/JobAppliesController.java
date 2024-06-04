package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.JobApplies;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.ApplicantListStatus;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.exception.IncorrectDateFormatException;
import am.itspace.jobboard.security.SecurityService;
import am.itspace.jobboard.service.JobAppliesService;
import am.itspace.jobboard.service.ResumeService;
import am.itspace.jobboard.specification.JobAppliesSpecification;
import am.itspace.jobboard.util.UrlSubStringUtil;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/job/applies")
public class JobAppliesController {

    private final JobAppliesService jobAppliesService;
    private final ResumeService resumeService;
    private final SecurityService securityService;

    @GetMapping("/{index}")
    public String jobApplies(@PathVariable("index") String indexStr, ModelMap modelMap) {
        User user = securityService.getCurrentUser();

        if (user != null && user.getRole() == Role.JOB_SEEKER) {
            if (indexStr == null || indexStr.isEmpty()) {
                return "redirect:/job/applies/1";
            }

            try {
                int index = Integer.parseInt(indexStr);

                if (resumeService.findByUserIdAndIsActiveTrue(user.getId()) == null) {
                    modelMap.addAttribute("createResumeMsg", "To view applied job history, you need to create a resume first.");
                    return "/profile/candidate-shortlisted-jobs";
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

                addAttributes(modelMap, null, null, null, jobApplies, 0, index);
                return "/profile/candidate-shortlisted-jobs";

            } catch (NumberFormatException e) {
                return "redirect:/job/applies/1";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "date", required = false) String sendDate,
                         @RequestParam(value = "searchIndexStr", required = false) String searchIndexStr,
                         ModelMap modelMap,
                         HttpServletRequest httpServletRequest) {

        User user = securityService.getCurrentUser();
        if (user != null && user.getRole() == Role.JOB_SEEKER) {
            try {

                int searchIndex = Integer.parseInt(searchIndexStr);
                String url = UrlSubStringUtil.removeLastCharacterFromQueryString(httpServletRequest);

                if (searchIndex <= 0 || searchIndex > Short.MAX_VALUE) {
                    return "redirect:/job/applies/1";
                }

                Specification<JobApplies> jobAppliesSpecification = JobAppliesSpecification.filterByStatusAndLastDate(status, sendDate, user.getId(), true);
                Page<JobApplies> jobApplies = jobAppliesService.findAll(jobAppliesSpecification, searchIndex);

                if (searchIndex > jobApplies.getTotalPages() && jobApplies.getTotalPages() != 0) {
                    return "redirect:/job/applies/1";
                }

                addAttributes(modelMap, status, sendDate, url, jobApplies, searchIndex, 0);
                modelMap.addAttribute("currentStatus", status);
                modelMap.addAttribute("currentDate", sendDate);
                return "/profile/candidate-shortlisted-jobs";

            } catch (NumberFormatException | IncorrectDateFormatException e) {
                return "redirect:/job/applies/1";
            }
        }
        return "redirect:/";
    }

    private void addAttributes(ModelMap modelMap, String status, String sandeDate, String url, Page<JobApplies> jobApplies, int searchIndex, int index) {
        modelMap.addAttribute("url", url);
        modelMap.addAttribute("jobApplies", jobApplies);
        modelMap.addAttribute("searchIndex", searchIndex);
        modelMap.addAttribute("index", index);
        modelMap.addAttribute("totalPages", jobApplies.getTotalPages());
        modelMap.addAttribute("jobAppliesCount", jobApplies.getTotalElements());
        modelMap.addAttribute("statuses", ApplicantListStatus.values());
        modelMap.addAttribute("currentStatus", status);
        modelMap.addAttribute("currentDate", sandeDate);
    }
}
