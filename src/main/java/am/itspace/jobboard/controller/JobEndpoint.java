package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.specification.JobSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-jobs")
public class JobEndpoint {

    private final JobService jobService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<Job>> userJobs(@RequestParam("page") int page,
                                              @RequestParam("userId") int userId,
                                              @RequestParam(value = "search",required = false) String search) {
        if (search != null && !search.isBlank()){
            Specification<Job> jobSpecification = JobSpecification.searchJobs(search, null, null,
                    null, userService.findById(userId),0, Double.MAX_VALUE, false);
            Page<Job> jobs = jobService.findAll(jobSpecification, page,4);
            return ResponseEntity.ok(jobs);
        }else {
            Specification<Job> jobSpecification = JobSpecification.searchJobs(null, null, null,
                    null, userService.findById(userId),0, Double.MAX_VALUE, false);
            Page<Job> jobs = jobService.findAll(jobSpecification, page,4);
            return ResponseEntity.ok(jobs);
        }
    }

}
