package am.itspace.jobboard.controller.admin;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.specification.JobSpecification;
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
@RequestMapping("/admin/jobs")
public class AdminJobController {

    private final JobService jobService;

    @GetMapping("/{indexStr}")
    public String getAdminJobsPage(@PathVariable String indexStr, ModelMap modelMap) {
        try {
            if (indexStr == null || indexStr.isEmpty()) {
                return "redirect:redirect:/admin/jobs/1";
            }
            int index = Integer.parseInt(indexStr);
            if (index <= 0) {
                return "redirect:redirect:/admin/jobs/1";
            }

            Page<Job> jobs = jobService.findAllJobs(index);

            if (index > jobs.getTotalPages() && jobs.getTotalPages() != 0) {
                return "redirect:redirect:/admin/jobs/1";
            }

            modelMap.put("page", "jobs");

            modelMap.put("index", index);
            modelMap.put("searchIndex", 0);
            modelMap.put("totalPages", jobs.getTotalPages());
            modelMap.put("jobCount", jobs.getNumberOfElements());
            modelMap.put("jobs", jobs);

        } catch (Exception e) {
            return "redirect:redirect:/admin/jobs/1";
        }
        return "admin/admin-jobs";
    }

    @GetMapping("/search")
    public String getAdminSearchJobPage(@RequestParam(value = "searchIndexStr", defaultValue = "1") String searchIndexStr,
                                        @RequestParam(value = "title", defaultValue = "") String title,
                                        ModelMap modelMap) {
        try {
            int searchIndex = Integer.parseInt(searchIndexStr);
            if (searchIndex <= 0) {
                throw new Exception();
            }

            Specification<Job> jobSpecification = JobSpecification.searchJobs(title, null, null, null, null, 0, Double.MAX_VALUE, null);
            Page<Job> jobs = jobService.findAllJobs(jobSpecification, searchIndex);

            if (searchIndex > jobs.getTotalPages() && jobs.getTotalPages() != 0) {
                return "redirect:/admin/jobs/1";
            }

            modelMap.put("page", "jobs");

            modelMap.put("index", 0);
            modelMap.put("searchIndex", searchIndex);
            modelMap.put("title", title);
            modelMap.put("totalPages", jobs.getTotalPages());
            modelMap.put("jobCount", jobs.getNumberOfElements());
            modelMap.put("jobs", jobs);

            return "admin/admin-jobs";
        } catch (Exception e) {
            return "redirect:/admin/jobs/1";
        }

    }

}
