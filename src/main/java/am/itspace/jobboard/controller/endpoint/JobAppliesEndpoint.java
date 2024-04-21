package am.itspace.jobboard.controller.endpoint;

import am.itspace.jobboard.service.JobAppliesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job-applies")
@RequiredArgsConstructor
public class JobAppliesEndpoint {

    private final JobAppliesService jobAppliesService;

    @PostMapping("/approve/{jobAppliesId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void approveJobApply(@PathVariable("jobAppliesId") int id) {
        jobAppliesService.changeStatusApproved(id);
    }

    @PostMapping("/reject/{jobAppliesId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rejectJobApply(@PathVariable("jobAppliesId") int id) {
        jobAppliesService.changeStatusRejected(id);
    }
}
