package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.ApplicantList;
import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobWishlist;
import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.entity.enums.Status;
import am.itspace.jobboard.entity.enums.WorkExperience;
import am.itspace.jobboard.security.SpringUser;
import am.itspace.jobboard.service.ApplicantListService;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.JobWishlistService;
import am.itspace.jobboard.service.ResumeService;
import am.itspace.jobboard.specification.JobSpecification;
import am.itspace.jobboard.util.PictureUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import static am.itspace.jobboard.entity.enums.Role.COMPANY_OWNER;
import static am.itspace.jobboard.entity.enums.Role.JOB_SEEKER;
import static am.itspace.jobboard.util.AddErrorMessageUtil.addErrorMessage;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;
    private final CategoryService categoryService;
    private final CompanyService companyService;
    private final ResumeService resumeService;
    private final ApplicantListService applicantListService;
    private final JobWishlistService jobWishlistService;

    @GetMapping("/{index}")
    public String jobPage(@PathVariable("index") String indexStr, ModelMap modelMap,
                          @AuthenticationPrincipal SpringUser springUser) {
        try {
            if (indexStr == null || indexStr.isEmpty()) {
                return "redirect:/jobs/1";
            }
            int index = Integer.parseInt(indexStr);
            if (index <= 0) {
                return "redirect:/jobs/1";
            }

            Page<Job> jobs = jobService.findAllByIsDeletedFalse(index);

            if (index > jobs.getTotalPages() && jobs.getTotalPages() != 0) {
                return "redirect:/jobs/1";
            }

            if (springUser != null) {
                List<JobWishlist> jobWishlists = jobWishlistService.findAllByUserId(springUser.getUser().getId());
                List<Job> jobList = new ArrayList<>();
                for (JobWishlist jobWishlist : jobWishlists) {
                    jobList.add(jobWishlist.getJob());
                }
                modelMap.addAttribute("favoritesJobs", jobList);
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
                            ModelMap modelMap,
                            @AuthenticationPrincipal SpringUser springUser) {
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
                    categoryService.findById(categoryId), null, fromSalary, toSalary, null);

            Page<Job> jobs = jobService.findAll(jobSpecification, searchIndex, 20);


            if (searchIndex > jobs.getTotalPages() && jobs.getTotalPages() != 0) {
                return "redirect:/jobs/1";
            }

            if (springUser != null) {
                List<JobWishlist> jobWishlists = jobWishlistService.findAllByUserId(springUser.getUser().getId());
                List<Job> jobList = new ArrayList<>();
                for (JobWishlist jobWishlist : jobWishlists) {
                    jobList.add(jobWishlist.getJob());
                }
                modelMap.addAttribute("favoritesJobs", jobList);
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

    @GetMapping("/search-my-jobs")
    public String searchMyJobs(@RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "category", required = false, defaultValue = "0") String categoryIdStr,
                               @RequestParam(value = "searchIndexStr", required = false) String searchIndexStr,
                               @RequestParam(value = "currentState", required = false, defaultValue = "") String currentState,
                               @AuthenticationPrincipal SpringUser springUser,
                               ModelMap modelMap,
                               HttpServletRequest httpServletRequest) {
        try {
            int categoryId = Integer.parseInt(categoryIdStr);
            int searchIndex = Integer.parseInt(searchIndexStr);
            Boolean isDeleted = null;
            if (!currentState.isBlank()) {
                isDeleted = Boolean.parseBoolean(currentState);
            }
            Specification<Job> jobSpecification = JobSpecification.searchJobs(title, null, null,
                    categoryService.findById(categoryId), springUser.getUser(), 0, Double.MAX_VALUE, isDeleted);
            Page<Job> jobs = jobService.findAll(jobSpecification, searchIndex, 20);
            if (searchIndex > jobs.getTotalPages() && jobs.getTotalPages() != 0) {
                return "redirect:/profile/jobs-manage/1";
            }

            String string = httpServletRequest.getQueryString();
            int length = string.length() - 1;
            String url = string.substring(0, length);

            modelMap.addAttribute("currentTitle", title);
            modelMap.addAttribute("currentCategoryId", categoryId);
            modelMap.addAttribute("currentState", String.valueOf(isDeleted));
            addAttributes(modelMap, url, jobs, searchIndex, 0);

            return "profile/manage-job";
        } catch (Exception e) {
            return "redirect:/profile/jobs-manage/1";
        }
    }

    @PostMapping("/create")
    public String createJob(@AuthenticationPrincipal SpringUser springUser,
                            @Valid @ModelAttribute Job job,
                            BindingResult bindingResult,
                            @RequestParam(value = "categoryId", defaultValue = "") String categoryIdStr,
                            @RequestParam(value = "jobStatus", defaultValue = "") String statusStr,
                            @RequestParam(value = "experience", defaultValue = "") String experienceStr,
                            @RequestParam(value = "picture", required = false) MultipartFile multipartFile,
                            RedirectAttributes redirectAttributes) {

        User user = springUser.getUser();

        if (bindingResult.hasErrors()) {
            bindingResults(bindingResult, redirectAttributes);
            return "redirect:/profile/jobs-create";
        }

        if (user != null && user.getRole() == Role.EMPLOYEE) {
            try {

                if (multipartFile != null && !multipartFile.isEmpty()) {
                    jobService.createJobForEmployee(job, user, categoryIdStr, statusStr, experienceStr, multipartFile);
                    return "redirect:/profile/jobs-manage/1";

                } else if (!PictureUtil.isFileSizeValid(multipartFile)) {
                    addFlashAttributes(redirectAttributes, "The job logo must be a maximum of 10MB in size.");
                    return "redirect:/profile/jobs-create";
                }

                addFlashAttributes(redirectAttributes, "Invalid job logo.");
                return "redirect:/profile/jobs-create";

            } catch (Exception e) {
                addFlashAttributes(redirectAttributes, "You should fill all the fields by right way.");
                return "redirect:/profile/jobs-create";
            }
        }
        return "redirect:/";
    }

    @PostMapping("/create/company")
    public String createJobForCompany(@AuthenticationPrincipal SpringUser springUser,
                                      @Valid @ModelAttribute Job job,
                                      BindingResult bindingResult,
                                      @RequestParam(value = "categoryId", defaultValue = "") String categoryIdStr,
                                      @RequestParam(value = "jobStatus", defaultValue = "") String statusStr,
                                      @RequestParam(value = "experience", defaultValue = "") String experienceStr,
                                      RedirectAttributes redirectAttributes) {
        User user = springUser.getUser();

        if (bindingResult.hasErrors()) {
            bindingResults(bindingResult, redirectAttributes);
            return "redirect:/profile/jobs-create";
        }

        if (user != null && user.getRole() == COMPANY_OWNER) {
            try {

                Company company = companyService.findCompanyByUserIdAndIsActiveTrue(user.getId());
                if (company != null) {
                    jobService.createJobForCompanyOwner(company, job, user, categoryIdStr, statusStr, experienceStr);
                    return "redirect:/profile/jobs-manage/1";
                }

                addFlashAttributes(redirectAttributes, "Create company, if you want to publish a job.");
                return "redirect:/profile/company";

            } catch (Exception e) {
                addFlashAttributes(redirectAttributes, "You should fill all the fields by right way.");
                return "redirect:/profile/jobs-create";
            }
        }
        return "redirect:/";
    }

    @PostMapping("/update/company")
    public String updateJobCompany(@AuthenticationPrincipal SpringUser springUser,
                                   @Valid @ModelAttribute Job job,
                                   BindingResult bindingResult,
                                   @RequestParam(value = "categoryId", defaultValue = "") String categoryIdStr,
                                   @RequestParam(value = "jobStatus", defaultValue = "") String statusStr,
                                   @RequestParam(value = "experience", defaultValue = "") String experienceStr,
                                   RedirectAttributes redirectAttributes) {

        User user = springUser.getUser();

        if (bindingResult.hasErrors()) {
            bindingResults(bindingResult, redirectAttributes);
            redirectAttributes.addFlashAttribute("job", jobService.findByIdAndIsDeletedFalse(job.getId()));
            return "redirect:/profile/jobs-create";
        }

        if (user != null && user.getRole() == COMPANY_OWNER) {
            try {

                int categoryId = Integer.parseInt(categoryIdStr);
                jobService.updateForCompanyOwner(job, categoryId, statusStr, experienceStr);
                return "redirect:/profile/jobs-manage/1";

            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("job", jobService.findByIdAndIsDeletedFalse(job.getId()));
                addFlashAttributes(redirectAttributes, "You should fill all the fields by right way.");
                return "redirect:/profile/jobs-create";
            }
        }
        return "redirect:/";
    }

    @PostMapping("/update")
    public String updateJob(@AuthenticationPrincipal SpringUser springUser,
                            @Valid @ModelAttribute Job job,
                            BindingResult bindingResult,
                            @RequestParam(value = "categoryId", defaultValue = "") String categoryIdStr,
                            @RequestParam(value = "jobStatus", defaultValue = "") String statusStr,
                            @RequestParam(value = "experience", defaultValue = "") String experienceStr,
                            @RequestParam(value = "picture", required = false) MultipartFile multipartFile,
                            RedirectAttributes redirectAttributes) {

        User user = springUser.getUser();

        if (bindingResult.hasErrors()) {
            bindingResults(bindingResult, redirectAttributes);
            redirectAttributes.addFlashAttribute("job", jobService.findByIdAndIsDeletedFalse(job.getId()));
            return "redirect:/profile/jobs-create";
        }

        if (user != null && user.getRole() == Role.EMPLOYEE) {
            try {
                int categoryId = Integer.parseInt(categoryIdStr);

                Job oldJob = jobService.findByIdAndIsDeletedFalse(job.getId());

                if ((multipartFile != null && !multipartFile.isEmpty()) || oldJob.getLogoName().equals(job.getLogoName())) {
                    jobService.updateForEmployee(job, oldJob, categoryId, statusStr, experienceStr, multipartFile);
                    return "redirect:/profile/jobs-manage/1";

                } else if (!PictureUtil.isFileSizeValid(multipartFile)) {
                    addFlashAttributes(redirectAttributes, "The job logo must be a maximum of 10MB in size.");
                    return "redirect:/profile/jobs-create";
                }

                addFlashAttributes(redirectAttributes, "Invalid job logo.");
                return "redirect:/profile/jobs-create";

            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("job", jobService.findByIdAndIsDeletedFalse(job.getId()));
                addFlashAttributes(redirectAttributes, "You should fill all the fields by right way.");
                return "redirect:/profile/jobs-create";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/item/{id}")
    public String singleJobPage(@PathVariable("id") String idStr, ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {

        try {
            int id = Integer.parseInt(idStr);
            Job job = jobService.getJobById(id);
            if (job != null) {
                modelMap.addAttribute("job", job);
                modelMap.addAttribute("jobs", jobService.findTop6());

                if (springUser != null) {
                    List<JobWishlist> jobWishlists = jobWishlistService.findAllByUserId(springUser.getUser().getId());
                    List<Job> jobList = new ArrayList<>();
                    for (JobWishlist jobWishlist : jobWishlists) {
                        jobList.add(jobWishlist.getJob());
                    }
                    modelMap.addAttribute("favoritesJobs", jobList);
                }
                return "job-single";
            } else {
                return "redirect:/jobs/1";
            }
        } catch (NumberFormatException e) {
            return "redirect:/jobs/1";
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable("id") String idStr, @AuthenticationPrincipal SpringUser springUser) {
        try {
            int id = Integer.parseInt(idStr);
            Job byId = jobService.findById(id);
            if (byId == null || byId.getUser().getId() != springUser.getUser().getId()) {
                throw new Exception();
            }
            jobService.deleteById(byId);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<?> returnJob(@PathVariable("id") String idStr, @AuthenticationPrincipal SpringUser springUser) {
        try {
            int id = Integer.parseInt(idStr);
            Job byId = jobService.findById(id);
            if (byId == null || byId.getUser().getId() != springUser.getUser().getId()) {
                throw new Exception();
            }
            jobService.recoverJobById(byId);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/apply/{id}")
    public String jobApply(@PathVariable("id") String idStr, RedirectAttributes redirectAttributes, @AuthenticationPrincipal SpringUser springUser) {
        try {
            int id = Integer.parseInt(idStr);
            Job job = jobService.getJobById(id);

            if (springUser == null || springUser.getUser().getRole() != JOB_SEEKER) {

                redirectAttributes.addFlashAttribute("errorMsg", "You cannot apply for this job.");
                return "redirect:/jobs/item/" + id;
            }

            Resume resume = resumeService.findByUserIdAndIsActiveTrue(springUser.getUser().getId());

            if (resume == null || !resume.isActive()) {
                redirectAttributes.addFlashAttribute("resumeMsg", "Create your Resume, for applying this job.");
                return "redirect:/jobs/item/" + id;
            }

            ApplicantList applyJob = applicantListService.findByJobIdAndResumeId(job.getId(), resume.getId());
            if (applyJob != null && applyJob.isActive()) {
                redirectAttributes.addFlashAttribute("applyJobMsg", "You have already applied this job.");
                return "redirect:/jobs/item/" + id;
            }

            applicantListService.save(job, resume);

            redirectAttributes.addFlashAttribute("msg", "Success. You have applied for the job.");
            return "redirect:/jobs/item/" + id;


        } catch (NumberFormatException e) {
            return "redirect:/jobs/1";
        }
    }

    @GetMapping("/favorites/{index}")
    public String favoritesJobs(@PathVariable("index") String indexStr,
                                ModelMap modelMap,
                                @AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null) {
            try {
                if (indexStr == null || indexStr.isEmpty()) {
                    return "redirect:/jobs/favorites/1";
                }

                int index = Integer.parseInt(indexStr);

                if (index <= 0) {
                    return "redirect:/jobs/favorites/1";
                }

                Page<JobWishlist> byUserid = jobWishlistService.findByUserId(index, springUser.getUser().getId());

                if (index > byUserid.getTotalPages() && byUserid.getTotalPages() != 0) {
                    return "redirect:/jobs/favorites/1";
                }

                modelMap.addAttribute("favoritesJobs", byUserid);
                modelMap.addAttribute("index", index);
                modelMap.addAttribute("totalPages", byUserid.getTotalPages());

                return "favorites-jobs";

            } catch (NumberFormatException e) {
                return "redirect:/jobs/favorites/1";
            }

        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/favorites/add/{idStr}")
    public ResponseEntity<?> addWishlist(@PathVariable("idStr") String idStr,
                                         @AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null) {
            try {
                int id = Integer.parseInt(idStr);

                Job job = jobService.findById(id);

                if (job != null && !job.isDeleted()) {
                    jobWishlistService.save(job, springUser.getUser());
                    return ResponseEntity.ok().build();
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/favorites/delete/{idStr}")
    public ResponseEntity<?> deleteWishlist(@PathVariable("idStr") String idStr,
                                            @AuthenticationPrincipal SpringUser springUser) {

        if (springUser != null) {
            try {
                int id = Integer.parseInt(idStr);

                Job job = jobService.findById(id);

                if (job != null && !job.isDeleted()) {
                    jobWishlistService.delete(job, springUser.getUser());
                    return ResponseEntity.ok().build();
                }

            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().build();
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

    private void bindingResults(BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        int errorCount = bindingResult.getErrorCount();
        if (errorCount > 1) {
            addFlashAttributes(redirectAttributes, "You should fill all the fields by right way.");
        } else if (errorCount == 1) {
            addErrorMessage(redirectAttributes, bindingResult);
        }
    }

    private void addFlashAttributes(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("msg", message);
    }
}
