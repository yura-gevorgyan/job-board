package am.itspace.jobboard.controller.admin;

import am.itspace.jobboard.entity.Category;
import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.ResumeService;
import am.itspace.jobboard.specification.ResumeSpecification;
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
@RequestMapping("/admin/resumes")
public class AdminResumeController {

    private final ResumeService resumeService;

    private final CategoryService categoryService;

    @GetMapping("/{indexStr}")
    public String getResumesPage(@PathVariable String indexStr, ModelMap modelMap) {
        try {
            if (indexStr == null || indexStr.isEmpty()) {
                return "redirect:/admin/resumes/1";
            }
            int index = Integer.parseInt(indexStr);
            if (index <= 0) {
                return "redirect:/admin/resumes/1";
            }

            Page<Resume> resumes = resumeService.findAllResumes(index);

            if (index > resumes.getTotalPages() && resumes.getTotalPages() != 0) {
                return "redirect:/admin/resumes/1";
            }

            modelMap.put("page", "resumes");

            modelMap.put("index", index);
            modelMap.put("searchIndex", 0);
            modelMap.put("totalPages", resumes.getTotalPages());
            modelMap.put("categories", categoryService.findAll());
            modelMap.put("resumeCount", resumes.getNumberOfElements());
            modelMap.put("resumes", resumes);

        } catch (Exception e) {
            return "redirect:/admin/resumes/1";
        }
        return "admin/admin-resumes";
    }

    @GetMapping("/search")
    public String getAdminSearchJobPage(@RequestParam(value = "searchIndexStr", defaultValue = "1") String searchIndexStr,
                                        @RequestParam(value = "profession", defaultValue = "") String profession,
                                        @RequestParam(value = "category", defaultValue = "0") String categoryIdStr,
                                        ModelMap modelMap) {
        try {
            int searchIndex = Integer.parseInt(searchIndexStr);
            int categoryId = Integer.parseInt(categoryIdStr);
            if (searchIndex <= 0) {
                throw new Exception();
            }
            Category category;
            category = categoryService.findById(categoryId);
            if (category != null) {
                modelMap.put("category", category.getId());
            } else {
                modelMap.put("category", "");
            }

            Specification<Resume> resumeSpecification = ResumeSpecification.searchResumes(profession, null, null, category, 0, Double.MAX_VALUE, null);
            Page<Resume> resumes = resumeService.findAllResumes(resumeSpecification, searchIndex);

            if (searchIndex > resumes.getTotalPages() && resumes.getTotalPages() != 0) {
                return "redirect:/admin/resumes/1";
            }

            modelMap.put("page", "resumes");

            modelMap.put("index", 0);
            modelMap.put("searchIndex", searchIndex);
            modelMap.put("profession", profession);
            modelMap.put("totalPages", resumes.getTotalPages());
            modelMap.put("resumeCount", resumes.getNumberOfElements());
            modelMap.put("resumes", resumes);
            modelMap.put("categories", categoryService.findAll());

            return "admin/admin-resumes";
        } catch (Exception e) {
            return "redirect:/admin/resumes/1";
        }
    }
}
