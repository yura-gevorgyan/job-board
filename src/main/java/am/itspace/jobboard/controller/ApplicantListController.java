package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.ApplicantList;
import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.ApplicantListStatus;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.security.SpringUser;
import am.itspace.jobboard.service.ApplicantListService;
import am.itspace.jobboard.service.ResumeService;
import am.itspace.jobboard.specification.ApplicantListSpecification;
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
@RequestMapping("/applicant/list")
public class ApplicantListController {

    private final ApplicantListService applicantListService;
    private final ResumeService resumeService;

    @GetMapping("/{index}")
    public String appliedJobs(@PathVariable("index") String indexStr,
                              @AuthenticationPrincipal SpringUser springUser,
                              ModelMap modelMap) {

        User user = springUser.getUser();
        if (user != null && user.getRole() == Role.JOB_SEEKER) {
            if (indexStr == null || indexStr.isEmpty()) {
                return "redirect:/applicant/list/1";
            }

            try {
                int index = Integer.parseInt(indexStr);

                if (index <= 0) {
                    return "redirect:/applicant/list/1";
                }

                Resume resume = resumeService.findByUserIdAndIsActiveTrue(user.getId());
                if (resume != null) {
                    Page<ApplicantList> applicantList = applicantListService.findAllByResumeIdAndIsActiveTrue(resume.getId(), index);

                    if (index > applicantList.getTotalPages() && applicantList.getTotalPages() != 0) {
                        return "redirect:/applicant/list/1";
                    }

                    addAttributes(modelMap, null, applicantList, 0, index);
                    return "/profile/candidate-applied-job";
                }

                modelMap.addAttribute("createResumeMsg", "For watch apply jobs history, for the first create resume");
                return "/profile/candidate-applied-job";

            } catch (NumberFormatException e) {
                return "redirect:/applicant/list/1";
            }
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
        Resume resume = resumeService.findByUserIdAndIsActiveTrue(user.getId());

        try {
            int searchIndex = Integer.parseInt(searchIndexStr);

            String string = httpServletRequest.getQueryString();
            int length = string.length() - 1;
            String url = string.substring(0, length);

            if (searchIndex <= 0 || searchIndex > Short.MAX_VALUE) {
                return "redirect:/applicant/list/1";
            }

            if (resume != null) {
                Specification<ApplicantList> applicantListSpecification = ApplicantListSpecification.filterByStatusAndLastDate(status, sendDate, resume.getId(), true);
                Page<ApplicantList> applicantLists = applicantListService.findAllByResumeIdAndIsActiveTrue(applicantListSpecification, searchIndex, resume.getId());

                if (searchIndex > applicantLists.getTotalPages() && applicantLists.getTotalPages() != 0) {
                    return "redirect:/applicant/list/1";
                }

                addAttributes(modelMap, url, applicantLists, searchIndex, 0);
                modelMap.addAttribute("currentStatus", status);
                modelMap.addAttribute("currentDate", sendDate);
                return "/profile/candidate-applied-job";
            }

            modelMap.addAttribute("createResumeMsg", "For watch apply jobs history, for the first create resume");
            return "/profile/candidate-applied-job";

        } catch (NumberFormatException e) {
            return "redirect:/applicant/list/1";
        }
    }

    private void addAttributes(ModelMap modelMap, String url, Page<ApplicantList> applicantLists, int searchIndex, int index) {
        modelMap.addAttribute("url", url);
        modelMap.addAttribute("applicantLists", applicantLists);
        modelMap.addAttribute("searchIndex", searchIndex);
        modelMap.addAttribute("index", index);
        modelMap.addAttribute("totalPages", applicantLists.getTotalPages());
        modelMap.addAttribute("applicantListCount", applicantLists.getTotalElements());
        modelMap.addAttribute("statuses", ApplicantListStatus.values());
    }
}
