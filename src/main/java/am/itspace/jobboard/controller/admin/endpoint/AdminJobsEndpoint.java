package am.itspace.jobboard.controller.admin.endpoint;

import am.itspace.jobboard.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/jobs")
public class AdminJobsEndpoint {

    private final JobService jobService;

    @PostMapping("/block/{jobId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void blockJob(@PathVariable int jobId) {
        jobService.blockById(jobId);
    }

    @PostMapping("/unlock/{jobId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlockJob(@PathVariable int jobId) {
        jobService.unlockById(jobId);
    }
}

