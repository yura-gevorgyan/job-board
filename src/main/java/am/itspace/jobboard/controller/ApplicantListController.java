
package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.ApplicantList;
import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.ApplicantListStatus;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.exception.IncorrectDateFormatException;
import am.itspace.jobboard.service.ApplicantListService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.ResumeService;
import am.itspace.jobboard.specification.ApplicantListSpecification;
import am.itspace.jobboard.security.SecurityService;
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
@RequestMapping("/applicant/list")
public class ApplicantListController {

    private final ApplicantListService applicantListService;
    private final ResumeService resumeService;
    private final JobService jobService;
    private final SecurityService securityService;

    @GetMapping("/{index}")
    public String appliedJobs(@PathVariable("index") String indexStr, ModelMap modelMap) {
        User user = securityService.getCurrentUser();

        if (indexStr == null || indexStr.isEmpty() || user == null) {
            return "redirect:/applicant/list/1";
        }
        try {

            int index = Integer.parseInt(indexStr);
            if (index <= 0) {
                return "redirect:/applicant/list/1";
            }

            if (user.getRole() == Role.JOB_SEEKER) {
                if (resumeService.findByUserIdAndIsActiveTrue(user.getId()) == null) {
                    modelMap.addAttribute("createResumeMsg", "To view applied jobs history, please create a resume.");
                    return "/profile/candidate-applied-job";
                }
                Page<ApplicantList> applicantList = applicantListService.findAllByResumeIdAndIsActiveTrue(resumeService.findByUserIdAndIsActiveTrue(user.getId()).getId(), index);
                if (index > applicantList.getTotalPages() && applicantList.getTotalPages() != 0) {
                    return "redirect:/applicant/list/1";
                }
                addAttributesToModel(modelMap, null, null, null, applicantList, 0, index);
                return "/profile/candidate-applied-job";

            } else if (user.getRole() == Role.EMPLOYEE || user.getRole() == Role.COMPANY_OWNER) {
                if (jobService.findByUserIdAndIsDeletedFalse(user.getId()) == null || jobService.findByUserIdAndIsDeletedFalse(user.getId()).isEmpty()) {
                    modelMap.addAttribute("createJob", "To view candidates, please create a job.");
                    return "/profile/applicant-list";
                }
                Page<ApplicantList> applicantList = applicantListService.findAllByToEmployerId(user.getId(), index);
                if (index > applicantList.getTotalPages() && applicantList.getTotalPages() != 0) {
                    return "redirect:/applicant/list/1";
                }
                addAttributesToModel(modelMap, null, null, null, applicantList, 0, index);
                return "/profile/applicant-list";
            }
        } catch (NumberFormatException e) {
            return "redirect:/applicant/list/1";
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
        Resume resume = resumeService.findByUserIdAndIsActiveTrue(user.getId());

        try {

            int searchIndex = Integer.parseInt(searchIndexStr);
            String url = UrlSubStringUtil.removeLastCharacterFromQueryString(httpServletRequest);

            if (searchIndex <= 0 || searchIndex > Short.MAX_VALUE) {
                return "redirect:/applicant/list/1";
            }

            Specification<ApplicantList> applicantListSpecification = getApplicantListSpecification(user, status, sendDate, resume);
            Page<ApplicantList> applicantLists = applicantListService.findAll(applicantListSpecification, searchIndex);

            if (searchIndex > applicantLists.getTotalPages() && applicantLists.getTotalPages() != 0) {
                return "redirect:/applicant/list/1";
            }

            addAttributesToModel(modelMap, status, sendDate, url, applicantLists, searchIndex, 0);
            return getViewForUserRole(user.getRole());

        } catch (NumberFormatException | IncorrectDateFormatException e) {
            return "redirect:/applicant/list/1";
        }
    }

    private String getViewForUserRole(Role role) {
        switch (role) {
            case JOB_SEEKER -> {
                return "/profile/candidate-applied-job";
            }
            case EMPLOYEE, COMPANY_OWNER -> {
                return "/profile/applicant-list";
            }
            default -> {
                return "redirect:/";
            }
        }
    }

    private Specification<ApplicantList> getApplicantListSpecification(User user, String status, String sendDate, Resume resume) {
        if (user.getRole() == Role.JOB_SEEKER && resume != null) {
            return ApplicantListSpecification.filterByStatusAndLastDate(status, sendDate, resume.getId(), 0, true);
        } else if (user.getRole() == Role.COMPANY_OWNER || user.getRole() == Role.EMPLOYEE) {
            return ApplicantListSpecification.filterByStatusAndLastDate(status, sendDate, 0, user.getId(), true);
        }
        return null;
    }

    private void addAttributesToModel(ModelMap modelMap, String status, String sandeDate, String url, Page<ApplicantList> applicantLists, int searchIndex, int index) {
        modelMap.addAttribute("url", url);
        modelMap.addAttribute("applicantLists", applicantLists);
        modelMap.addAttribute("searchIndex", searchIndex);
        modelMap.addAttribute("index", index);
        modelMap.addAttribute("totalPages", applicantLists.getTotalPages());
        modelMap.addAttribute("applicantListCount", applicantLists.getTotalElements());
        modelMap.addAttribute("statuses", ApplicantListStatus.values());
        modelMap.addAttribute("currentStatus", status);
        modelMap.addAttribute("currentDate", sandeDate);
    }
}
