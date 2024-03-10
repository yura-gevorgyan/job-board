package am.itspace.jobboard.controller.admin;

import am.itspace.jobboard.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/jobs")
public class AdminJobController {

    private final JobService jobService;

    @GetMapping("/{indexStr}")
    public String getAdminJobsPage(@PathVariable String indexStr, ModelMap modelMap) {
        int totalPages = jobService.getTotalPages();
        int index;
        try {
            index = Integer.parseInt(indexStr);
            if (index <= 0 || (index > totalPages && totalPages != 0)) {
                return "redirect:/admin/jobs/1";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/jobs/1";
        }

        modelMap.put("page", "jobs");

        modelMap.put("index", index);
        modelMap.put("searchIndex", 0);
        modelMap.put("totalPages", totalPages);
        modelMap.put("jobCount", jobService.getJobCount());
        modelMap.put("jobs", jobService.getJobsFromNToM(index));
        return "admin/admin-jobs";
    }

    @GetMapping("/search")
    public String getAdminSearchJobPage(@RequestParam(value = "searchIndexStr", defaultValue = "1") String searchIndexStr,
                                        @RequestParam(value = "title", defaultValue = "") String title, ModelMap modelMap) {

        int totalPagesOfSearchTitle = jobService.getTotalPagesOfSearch(title);
        int searchIndex;

        modelMap.put("page", "jobs");

        try {
            searchIndex = Integer.parseInt(searchIndexStr);
            if (searchIndex <= 0 || searchIndex > totalPagesOfSearchTitle) {
                return "redirect:/admin/jobs/1";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/jobs/1";
        }

        modelMap.put("index", 0);
        modelMap.put("searchIndex", searchIndex);
        modelMap.put("title", title);
        modelMap.put("totalPages", totalPagesOfSearchTitle);
        modelMap.put("jobCount", jobService.getJobCountOfTitle(title));
        modelMap.put("jobs", jobService.getJobsFromNToMForSearch(searchIndex, title));
        return "admin/admin-jobs";
    }

}
