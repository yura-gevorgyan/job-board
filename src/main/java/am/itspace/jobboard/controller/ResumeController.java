package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobApplies;
import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.ResumeWishlist;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.entity.enums.Status;
import am.itspace.jobboard.entity.enums.WorkExperience;
import am.itspace.jobboard.security.SecurityService;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.JobAppliesService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.ResumeService;
import am.itspace.jobboard.service.ResumeWishlistService;
import am.itspace.jobboard.specification.ResumeSpecification;
import am.itspace.jobboard.util.PictureUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
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

import static am.itspace.jobboard.util.AddErrorMessageUtil.addErrorMessage;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/resumes")
public class ResumeController {

    private final ResumeService resumeService;
    private final CategoryService categoryService;
    private final JobService jobService;
    private final JobAppliesService jobAppliesService;
    private final ResumeWishlistService resumeWishlistService;
    private final SecurityService securityService;

    @GetMapping("/{index}")
    public String resumesPage(@PathVariable("index") String indexStr, ModelMap modelMap) {
        User user = securityService.getCurrentUser();

        try {
            int index = Integer.parseInt(indexStr);

            if (index <= 0) {
                return "redirect:/resumes/1";
            }

            Page<Resume> resumes = resumeService.findAllByActiveTrue(index);

            if (index > resumes.getTotalPages() && resumes.getTotalPages() != 0) {
                return "redirect:/resumes/1";
            }

            if (user != null) {
                List<ResumeWishlist> resumeWishlist = resumeWishlistService.findAllByUserId(user.getId());
                List<Resume> resumeList = new ArrayList<>();
                for (ResumeWishlist wishlist : resumeWishlist) {
                    resumeList.add(wishlist.getResume());
                }
                modelMap.addAttribute("favoritesResumes", resumeList);
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
        User user = securityService.getCurrentUser();
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

            if (user != null) {
                List<ResumeWishlist> resumeWishlist = resumeWishlistService.findAllByUserId(user.getId());
                List<Resume> resumeList = new ArrayList<>();
                for (ResumeWishlist wishlist : resumeWishlist) {
                    resumeList.add(wishlist.getResume());
                }
                modelMap.addAttribute("favoritesResumes", resumeList);
            }
            return "job-seeker-list";

        } catch (IllegalArgumentException e) {
            return "redirect:/resumes/1";
        }
    }

    @GetMapping("/item/{id}")
    public String singleJobPage(@PathVariable("id") String idStr, ModelMap modelMap) {
        try {

            int id = Integer.parseInt(idStr);
            Resume resume = resumeService.getById(id);

            if (resume != null) {
                modelMap.addAttribute("resume", resume);
                modelMap.addAttribute("resumes", resumeService.findTop6());
                return "resume";

            } else {
                return "redirect:/resumes/1";
            }

        } catch (NumberFormatException e) {
            return "redirect:/resumes/1";
        }
    }

    @GetMapping("/apply/{id}")
    public String jobApply(@PathVariable("id") String idStr, RedirectAttributes redirectAttributes, @RequestParam(value = "jobId", required = false) String jobIdStr) {
        User user = securityService.getCurrentUser();

        try {
            int id = Integer.parseInt(idStr);
            Resume resume = resumeService.getById(id);

            if (user == null || user.getRole() == Role.JOB_SEEKER) {
                redirectAttributes.addFlashAttribute("errorMsg", "You cannot apply for this resume.");
                return "redirect:/resumes/item/" + id;
            }

            if (jobIdStr != null) {
                int jobId = Integer.parseInt(jobIdStr);

                Job job = jobService.getJobById(jobId);
                JobApplies jobApplies = jobAppliesService.findByJobIdAndUserIdAndIsActiveTrue(job.getId(), resume.getUser().getId());
                if (jobApplies != null) {
                    redirectAttributes.addFlashAttribute("applyResumeMsg", "You have already applied for this resume.");
                    return "redirect:/resumes/item/" + id;
                }

                jobAppliesService.save(job, resume.getUser());

                redirectAttributes.addFlashAttribute("msg", "Success. You have applied for the job.");
                log.info("User by {} id, has applied for job by {} id", user.getId(), job.getId());
                return "redirect:/resumes/item/" + id;
            } else {
                return "redirect:/resumes/item/" + id;
            }
        } catch (NumberFormatException e) {
            return "redirect:/resumes/1";
        }
    }

    @PostMapping("/create")
    public String createResume(@Valid @ModelAttribute Resume resume,
                               BindingResult bindingResult,
                               @RequestParam(value = "picture", required = false) MultipartFile multipartFile,
                               RedirectAttributes redirectAttributes) {
        User user = securityService.getCurrentUser();

        if (resumeService.findByUserIdAndIsActiveTrue(user.getId()) != null) {
            return "redirect:/profile/resume";
        }

        if (multipartFile == null || multipartFile.isEmpty() || resume.getCategory() == null ||
                resume.getGender() == null || resume.getWorkExperience() == null ||
                resume.getCategory().toString().isEmpty() || resume.getGender().toString().isEmpty() ||
                resume.getWorkExperience().toString().isEmpty()) {
            redirectAttributes.addFlashAttribute("msg", "Please fill all required fields.");
            return "redirect:/profile/resume";

        } else if (!PictureUtil.isFileSizeValid(multipartFile)) {
            redirectAttributes.addFlashAttribute("msg", "The resume logo must be a maximum of 10MB in size.");
            return "redirect:/profile/resume";
        }

        resume.setUser(user);
        resume.setCategory(categoryService.findById(resume.getCategory().getId()));

        if (bindingResult.hasErrors()) {
            addErrorMessage(redirectAttributes, bindingResult);
            return "redirect:/profile/resume";
        }

        resumeService.create(resume, multipartFile);
        log.info("User by {} id, has created a resume", user.getId());
        return "redirect:/profile/resume";
    }

    @PostMapping("/update")
    public String updateResume(@Valid @ModelAttribute Resume resume,
                               BindingResult bindingResult,
                               @RequestParam(value = "picture", required = false) MultipartFile multipartFile,
                               RedirectAttributes redirectAttributes) {
        User user = securityService.getCurrentUser();

        if (resume == null) {
            return "redirect:/profile/resume";
        }

        Resume originalResume = resumeService.findByUserIdAndIsActiveTrue(user.getId());

        if (multipartFile == null || multipartFile.isEmpty() && !originalResume.getPicName().equals(resume.getPicName())) {
            redirectAttributes.addFlashAttribute("msg", "Invalid Resume Picture, Please Try Again.");
            return "redirect:/profile/resume";
        }

        if ((resume.getUser() == null || resume.getCategory() == null) || (resume.getUser().getId() != user.getId()) || (resume.getId() != originalResume.getId())) {
            redirectAttributes.addFlashAttribute("msg", "No Way Modify Real Dates.");
            return "redirect:/profile/resume";
        }

        if (bindingResult.hasErrors()) {
            addErrorMessage(redirectAttributes, bindingResult);
            return "redirect:/profile/resume";
        }

        resumeService.update(resume, multipartFile);
        log.info("User by {} id, has updated the resume", user.getId());
        return "redirect:/profile/resume";
    }

    @GetMapping("/favorites/{index}")
    public String favoritesJobs(@PathVariable("index") String indexStr, ModelMap modelMap) {
        User user = securityService.getCurrentUser();

        if (user != null) {
            try {
                if (indexStr == null || indexStr.isEmpty()) {
                    return "redirect:/resumes/favorites/1";
                }

                int index = Integer.parseInt(indexStr);

                if (index <= 0) {
                    return "redirect:/resumes/favorites/1";
                }

                Page<ResumeWishlist> byUserid = resumeWishlistService.findByUserId(index, user.getId());

                if (index > byUserid.getTotalPages() && byUserid.getTotalPages() != 0) {
                    return "redirect:/resumes/favorites/1";
                }

                modelMap.addAttribute("favoritesResumes", byUserid);
                modelMap.addAttribute("index", index);
                modelMap.addAttribute("totalPages", byUserid.getTotalPages());

                return "favorites-resumes";

            } catch (NumberFormatException e) {
                return "redirect:/jobs/favorites/1";
            }

        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/favorites/add/{idStr}")
    public ResponseEntity<?> addWishlist(@PathVariable("idStr") String idStr) {
        User user = securityService.getCurrentUser();

        if (user != null) {
            try {

                int id = Integer.parseInt(idStr);
                Resume resume = resumeService.findById(id);

                if (resume != null && resume.isActive()) {
                    resumeWishlistService.save(resume, user);
                    log.info("User by {} id, has added the resume by {} id, in wishlist", user.getId(), resume.getId());
                    return ResponseEntity.ok().build();
                }

            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/favorites/delete/{idStr}")
    public ResponseEntity<?> deleteWishlist(@PathVariable("idStr") String idStr) {
        User user = securityService.getCurrentUser();

        if (user != null) {
            try {

                int id = Integer.parseInt(idStr);
                Resume resume = resumeService.findById(id);

                if (resume != null && resume.isActive()) {
                    resumeWishlistService.delete(resume, user);
                    log.info("User by {} id, has deleted the resume by {} id, from wishlist", user.getId(), resume.getId());
                    return ResponseEntity.ok().build();
                }

            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().build();
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
