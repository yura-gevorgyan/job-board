package am.itspace.jobboard.controller.admin.endpoint;

import am.itspace.jobboard.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/jobs")
public class AdminJobsEndpoint {

    private final JobService jobService;

    @PostMapping("/block/{jobId}")
    public ResponseEntity<Void> blockJob(@PathVariable int jobId) {
        jobService.blockById(jobId);
        log.info("Job by {} id, was blocked by Admin", jobId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/unlock/{jobId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> unlockJob(@PathVariable int jobId) {
        jobService.unlockById(jobId);
        log.info("Job by {} id, was unlocked by Admin", jobId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

