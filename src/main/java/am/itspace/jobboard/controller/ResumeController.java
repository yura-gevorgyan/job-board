package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobApplies;
import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.entity.enums.Status;
import am.itspace.jobboard.entity.enums.WorkExperience;
import am.itspace.jobboard.security.SpringUser;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.JobAppliesService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.ResumeService;
import am.itspace.jobboard.specification.ResumeSpecification;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/resumes")
public class ResumeController {

    private final ResumeService resumeService;
    private final CategoryService categoryService;
    private final JobService jobService;
    private final JobAppliesService jobAppliesService;

    @GetMapping("/{index}")
    public String resumesPage(@PathVariable("index") String indexStr, ModelMap modelMap) {
        try {
            int index = Integer.parseInt(indexStr);

            if (index <= 0) {
                return "redirect:/resumes/1";
            }

            Page<Resume> resumes = resumeService.findAllByActiveTrue(index);

            if (index > resumes.getTotalPages() && resumes.getTotalPages() != 0) {
                return "redirect:/resumes/1";
            }

            addAttributes(modelMap, resumes, index, 0, null);

            return "job-seeker-list";
        } catch (NumberFormatException e) {
            return "redirect:/resumes/1";
        }
    }


    @GetMapping("/search")
    public String resumeSearch(@RequestParam(value = "profession", required = false) String profession,
                               @RequestParam(value = "category", required = false, defaultValue = "0") String categoryIdStr,
                               @RequestParam(value = "gender", required = false) String genderStr,
                               @RequestParam(value = "experiences", required = false) List<String> experiences,
                               @RequestParam(value = "searchIndexStr", required = false) String searchIndexStr,
                               @RequestParam(value = "fromSalary", required = false, defaultValue = "0") String fromSalaryStr,
                               @RequestParam(value = "toSalary", required = false, defaultValue = "100000000") String toSalaryStr,
                               HttpServletRequest httpServletRequest,
                               ModelMap modelMap) {

        List<WorkExperience> experienceList = new ArrayList<>();

        try {
            int searchIndex = Integer.parseInt(searchIndexStr);
            int categoryId = Integer.parseInt(categoryIdStr);
            double fromSalary = Double.parseDouble(fromSalaryStr);
            double toSalary = Double.parseDouble(toSalaryStr);


            if (experiences != null && !experiences.isEmpty()) {
                for (String experience : experiences) {
                    experienceList.add(WorkExperience.valueOf(experience));
                }
            }

            String string = httpServletRequest.getQueryString();
            int length = string.length() - 1;
            String url = string.substring(0, length);

            if (searchIndex <= 0 || searchIndex > Short.MAX_VALUE) {
                return "redirect:/resumes/1";
            }

            Specification<Resume> resumeSpecification = ResumeSpecification.searchResumes(profession, experienceList, genderStr,
                    categoryService.findById(categoryId), fromSalary, toSalary, true);

            Page<Resume> resumes = resumeService.findAll(resumeSpecification, searchIndex);


            if (searchIndex > resumes.getTotalPages() && resumes.getTotalPages() != 0) {
                return "redirect:/resumes/1";
            }

            addAttributes(modelMap, resumes, 0, searchIndex, url);

            modelMap.addAttribute("currentProfession", profession);
            modelMap.addAttribute("currentCategoryId", categoryId);
            modelMap.addAttribute("currentFromSalary", (int) fromSalary);
            modelMap.addAttribute("currentGender", genderStr);
            modelMap.addAttribute("currentToSalary", (int) toSalary);
            modelMap.addAttribute("currentExperience", experienceList);
            return "job-seeker-list";

        } catch (IllegalArgumentException e) {
            return "redirect:/resumes/1";
        }
    }

    @GetMapping("/item/{id}")
    public String singleJobPage(@PathVariable("id") String idStr,
                                ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {

        try {
            int id = Integer.parseInt(idStr);

            Resume resume = resumeService.getById(id);
            if (resume != null) {
                modelMap.addAttribute("resume", resume);
                modelMap.addAttribute("resumes", resumeService.findTop6());
                if (springUser != null) {
                    List<Job> jobs = jobService.findByUserIdAndIsDeletedFalse(springUser.getUser().getId());
                    modelMap.addAttribute("totalPages", (int) Math.ceil((double) jobs.size() / 4));
                    modelMap.addAttribute("pageSize", 4);
                    modelMap.addAttribute("currentPage", 1);
                    modelMap.addAttribute("userJobs", jobs);
                }
                return "resume";
            } else {
                return "redirect:/resumes/1";
            }
        } catch (NumberFormatException e) {
            return "redirect:/resumes/1";
        }
    }

    @GetMapping("/apply/{id}")
    public String jobApply(@PathVariable("id") String idStr, RedirectAttributes redirectAttributes,
                           @RequestParam(value = "jobId",required = false) String jobIdStr,
                           @AuthenticationPrincipal SpringUser springUser) {
        try {
            int id = Integer.parseInt(idStr);
            Resume resume = resumeService.getById(id);

            if (springUser == null || springUser.getUser().getRole() == Role.JOB_SEEKER) {
                redirectAttributes.addFlashAttribute("errorMsg", "You can not to apply this resume.");
                return "redirect:/resumes/item/" + id;
            }

            List<Job> byUserId = jobService.findByUserIdAndIsDeletedFalse(springUser.getUser().getId());

            if (byUserId == null || byUserId.isEmpty()) {
                redirectAttributes.addFlashAttribute("jobMsg", "For apply this job create job.");
                return "redirect:/resumes/item/" + id;
            }

            if (jobIdStr != null){
                int jobId = Integer.parseInt(jobIdStr);

                Job job = jobService.getJobById(jobId);
                JobApplies jobApplies = jobAppliesService.findByJobIdAndUserIdAndIsActiveTrue(job.getId(), resume.getUser().getId());
                if (jobApplies != null){
                    redirectAttributes.addFlashAttribute("applyResumeMsg", "You have already applied this resume.");
                    return "redirect:/resumes/item/" + id;
                }

                jobAppliesService.save(job,resume.getUser());

                redirectAttributes.addFlashAttribute("msg", "Success. You apply for a job.");
                return "redirect:/resumes/item/" + id;
            }else {
                return "redirect:/resumes/item/" + id;
            }




        } catch (NumberFormatException e) {
            return "redirect:/resumes/1";
        }
    }

    @PostMapping("/create")
    public String createResume(@ModelAttribute Resume resume,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal SpringUser springUser,
                               @RequestParam(value = "picture", required = false) MultipartFile multipartFile,
                               RedirectAttributes redirectAttributes) {
        if (springUser.getUser() == null) {
            return "redirect:/";
        }

        if (multipartFile == null || multipartFile.isEmpty() || resume.getCategory() == null ||
                resume.getGender() == null || resume.getWorkExperience() == null ||
                resume.getCategory().toString().isEmpty() || resume.getGender().toString().isEmpty() ||
                resume.getWorkExperience().toString().isEmpty()) {
            redirectAttributes.addFlashAttribute("msg", "Please fill all required fields!");
            return "redirect:/profile/resume";
        }

        resume.setUser(springUser.getUser());
        resume.setCategory(categoryService.findById(resume.getCategory().getId()));

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("msg", "You are not allowed to modify real dates!");
            return "redirect:/profile/resume";
        }

        resumeService.create(resume, multipartFile);
        return "redirect:/profile/resume";
    }

    @PostMapping("/update")
    public String updateResume(@ModelAttribute Resume resume,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal SpringUser springUser,
                               @RequestParam(value = "picture", required = false) MultipartFile multipartFile,
                               RedirectAttributes redirectAttributes) {
        if (springUser.getUser() == null) {
            return "redirect:/";
        }

        if (resume == null) {
            return "redirect:/profile/resume";
        }

        Resume originalResume = resumeService.findByUserIdAndIsActiveTrue(springUser.getUser().getId());

        if (multipartFile == null || multipartFile.isEmpty() || StringUtils.isEmpty(multipartFile.getOriginalFilename()) ||
                resume.getUser().getId() != springUser.getUser().getId() ||
                resume.getId() != originalResume.getId()) {
            return "redirect:/profile/resume";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("msg", "You are not allowed to modify real dates!");
            return "redirect:/profile/resume";
        }

        resumeService.update(resume, multipartFile);
        return "redirect:/profile/resume";
    }

    private void addAttributes(ModelMap modelMap, Page<Resume> resumes, int index, int searchIndex, String url) {
        modelMap.addAttribute("url", url);
        modelMap.addAttribute("resumes", resumes);
        modelMap.addAttribute("index", index);
        modelMap.addAttribute("searchIndex", searchIndex);
        modelMap.addAttribute("totalPages", resumes.getTotalPages());
        modelMap.addAttribute("resumeCount", resumes.getTotalElements());
        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("statuses", Status.values());
        modelMap.addAttribute("experiences", WorkExperience.values());
    }
}
