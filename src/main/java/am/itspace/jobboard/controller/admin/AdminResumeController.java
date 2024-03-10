package am.itspace.jobboard.controller.admin;

import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.ResumeService;
import lombok.RequiredArgsConstructor;
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
        int totalPages = resumeService.getTotalPages();
        int index;
        try {
            index = Integer.parseInt(indexStr);
            if (index <= 0 || (index > totalPages && totalPages != 0)) {
                return "redirect:/admin/resumes/1";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/resumes/1";
        }

        modelMap.put("page", "resumes");

        modelMap.put("index", index);
        modelMap.put("searchIndex", 0);
        modelMap.put("totalPages", totalPages);
        modelMap.put("categories", categoryService.findAll());
        modelMap.put("resumeCount", resumeService.getResumeCount());
        modelMap.put("resumes", resumeService.getResumesFromNToM(index));
        return "admin/admin-resumes";
    }

    @GetMapping("/search")
    public String getAdminSearchJobPage(@RequestParam(value = "searchIndexStr", defaultValue = "1") String searchIndexStr,
                                        @RequestParam(value = "email") String email,
                                        @RequestParam(value = "category", defaultValue = "") String categoryIdStr, ModelMap modelMap) {
        int categoryId;
        try {
            categoryId = Integer.parseInt(categoryIdStr);
        } catch (NumberFormatException e) {
            categoryId = 0;
        }
        int totalPagesOfSearchCategoryName = resumeService.getTotalPagesOfSearch(categoryId, email);
        int searchIndex;

        modelMap.put("page", "resumes");

        try {
            searchIndex = Integer.parseInt(searchIndexStr);
            if (searchIndex <= 0 || searchIndex > totalPagesOfSearchCategoryName) {
                return "redirect:/admin/resumes/1";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/resumes/1";
        }

        modelMap.put("index", 0);
        modelMap.put("searchIndex", searchIndex);
        modelMap.put("email", email);
        if (categoryService.exists(categoryId)) {
            modelMap.put("category", categoryId);
        } else {
            modelMap.put("category", "");
        }
        modelMap.put("categories", categoryService.findAll());
        modelMap.put("totalPages", totalPagesOfSearchCategoryName);
        modelMap.put("resumeCount", resumeService.getResumeCountOfCategoryUserEmail(categoryId, email));
        modelMap.put("resumes", resumeService.getResumesFromNToMForSearch(searchIndex, categoryId, email));
        return "admin/admin-resumes";
    }
}
