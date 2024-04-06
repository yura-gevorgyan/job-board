package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.ApplicantList;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.entity.enums.Status;
import am.itspace.jobboard.entity.enums.WorkExperience;
import am.itspace.jobboard.security.SpringUser;
import am.itspace.jobboard.service.ApplicantListService;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.ResumeService;
import am.itspace.jobboard.specification.JobSpecification;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;
    private final CategoryService categoryService;
    private final CompanyService companyService;
    private final ResumeService resumeService;
    private final ApplicantListService applicantListService;

    @GetMapping("/{index}")
    public String jobPage(@PathVariable("index") String indexStr, ModelMap modelMap) {
        try {
            if (indexStr == null || indexStr.isEmpty()) {
                return "redirect:/jobs/1";
            }
            int index = Integer.parseInt(indexStr);
            if (index <= 0) {
                return "redirect:/jobs/1";
            }

            Page<Job> jobs = jobService.findAllByIsDeletedFalse(index);

            if (index > jobs.getTotalPages() && jobs.getTotalPages() != 0 ) {
                return "redirect:/jobs/1";
            }

            addAttributes(modelMap, null, jobs, 0, index);

            return "job-list";
        } catch (NumberFormatException e) {
            return "redirect:/jobs/1";
        }
    }

    @GetMapping("/search")
    public String jobSearch(@RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "category", required = false, defaultValue = "0") String categoryIdStr,
                            @RequestParam(value = "statuses", required = false) List<String> statuses,
                            @RequestParam(value = "experiences", required = false) List<String> experiences,
                            @RequestParam(value = "searchIndexStr", required = false) String searchIndexStr,
                            @RequestParam(value = "fromSalary", required = false, defaultValue = "0") String fromSalaryStr,
                            @RequestParam(value = "toSalary", required = false, defaultValue = "100000000") String toSalaryStr,
                            HttpServletRequest httpServletRequest,
                            ModelMap modelMap) {
        List<Status> statusList = new ArrayList<>();
        List<WorkExperience> experienceList = new ArrayList<>();

        try {
            int searchIndex = Integer.parseInt(searchIndexStr);
            int categoryId = Integer.parseInt(categoryIdStr);
            double fromSalary = Double.parseDouble(fromSalaryStr);
            double toSalary = Double.parseDouble(toSalaryStr);

            if (statuses != null && !statuses.isEmpty()) {
                for (String status : statuses) {
                    statusList.add(Status.valueOf(status));
                }
            }

            if (experiences != null && !experiences.isEmpty()) {
                for (String experience : experiences) {
                    experienceList.add(WorkExperience.valueOf(experience));
                }
            }

            String string = httpServletRequest.getQueryString();
            int length = string.length() - 1;
            String url = string.substring(0, length);

            if (searchIndex <= 0 || searchIndex > Short.MAX_VALUE) {
                return "redirect:/jobs/1";
            }

            Specification<Job> jobSpecification = JobSpecification.searchJobs(title, experienceList, statusList,
                    categoryService.findById(categoryId), fromSalary, toSalary, false);

            Page<Job> jobs = jobService.findAll(jobSpecification, searchIndex);


            if (searchIndex > jobs.getTotalPages() && jobs.getTotalPages() != 0) {
                return "redirect:/jobs/1";
            }

            addAttributes(modelMap, url, jobs, searchIndex, 0);

            modelMap.addAttribute("currentTitle", title);
            modelMap.addAttribute("currentCategoryId", categoryId);
            modelMap.addAttribute("currentFromSalary", (int) fromSalary);
            modelMap.addAttribute("currentToSalary", (int) toSalary);
            modelMap.addAttribute("currentStatus", statusList);
            modelMap.addAttribute("currentExperience", experienceList);
            return "job-list";

        } catch (IllegalArgumentException e) {
            return "redirect:/jobs/1";
        }
    }

    @PostMapping("/create")
    public String createJob(@AuthenticationPrincipal SpringUser springUser,
                            @ModelAttribute Job job,
                            RedirectAttributes redirectAttributes) {

        if (springUser.getUser() == null) {
            return "redirect:/";
        }

        if (springUser.getUser().getRole() == Role.COMPANY_OWNER && companyService.findCompanyByUserIdAndIsActiveTrue(springUser.getUser().getId()) == null) {
            redirectAttributes.addFlashAttribute("msg", "For creating job please create Company!");
            return "redirect:/profile/company";
        }

        if (addRedirectAttribute(job) != null) {
            redirectAttributes.addFlashAttribute("msg", addRedirectAttribute(job));
            return "redirect:/profile/jobs-create";
        }

        jobService.save(job);
        return "redirect:/profile/jobs-manage";
    }

    @GetMapping("/item/{id}")
    public String singleJobPage(@PathVariable("id") String idStr, ModelMap modelMap) {

        try {
            int id = Integer.parseInt(idStr);
            Job job = jobService.getJobById(id);
            if (job != null) {
                modelMap.addAttribute("job", job);
                modelMap.addAttribute("jobs", jobService.findTop6());
                return "job-single";
            } else {
                return "redirect:/jobs/1";
            }
        } catch (NumberFormatException e) {
            return "redirect:/jobs/1";
        }
    }

    @GetMapping("/apply/{id}")
    public String jobApply(@PathVariable("id") String idStr, RedirectAttributes redirectAttributes, @AuthenticationPrincipal SpringUser springUser) {
        try {
            int id = Integer.parseInt(idStr);
            Job job = jobService.getJobById(id);

            if (springUser == null || springUser.getUser().getRole() != Role.JOB_SEEKER) {

                redirectAttributes.addFlashAttribute("errorMsg", "You can not to apply this job.");
                return "redirect:/jobs/item/" + id;
            }

            Resume resume = resumeService.findByUserIdAndIsActiveTrue(springUser.getUser().getId());
            if (resume == null || !resume.isActive()) {
                redirectAttributes.addFlashAttribute("resumeMsg", "For apply this job please create your resume.");
                return "redirect:/jobs/item/" + id;
            }

            ApplicantList applyJob = applicantListService.findByEmployerIdAndResumeId(job.getUser().getId(), resume.getId());
            if (applyJob != null && applyJob.isActive()) {
                redirectAttributes.addFlashAttribute("applyJobMsg", "You have already applied this job.");
                return "redirect:/jobs/item/" + id;
            }

            applicantListService.save(job, resume);

            redirectAttributes.addFlashAttribute("msg", "Success. You apply for a job.");
            return "redirect:/jobs/item/" + id;


        } catch (NumberFormatException e) {
            return "redirect:/jobs/1";
        }
    }

    private void addAttributes(ModelMap modelMap, String url, Page<Job> jobs, int searchIndex, int index) {
        modelMap.addAttribute("url", url);
        modelMap.addAttribute("jobs", jobs);
        modelMap.addAttribute("searchIndex", searchIndex);
        modelMap.addAttribute("index", index);
        modelMap.addAttribute("totalPages", jobs.getTotalPages());
        modelMap.addAttribute("jobCount", jobs.getTotalElements());
        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("statuses", Status.values());
        modelMap.addAttribute("experiences", WorkExperience.values());
    }

    private String addRedirectAttribute(Job job) {
        if (job.getCategory() == null || job.getCategory().toString().isEmpty()) {
            return "Choose Job Category!";
        } else if (job.getStatus() == null || job.getStatus().toString().isEmpty()) {
            return "Choose Status!";
        } else if (job.getWorkExperience() == null || job.getWorkExperience().toString().isEmpty()) {
            return "Choose Work Experience!";
        }
        return null;
    }
}
