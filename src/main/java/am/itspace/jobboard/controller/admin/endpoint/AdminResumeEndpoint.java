package am.itspace.jobboard.controller.admin.endpoint;

import am.itspace.jobboard.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/resumes")
public class AdminResumeEndpoint {

    private final ResumeService resumeService;

    @DeleteMapping("/delete/{resumeId}")
    public ResponseEntity<Void> deleteResume(@PathVariable int resumeId) {
        resumeService.deleteById(resumeId);
        log.info("Resume by {} id, was deleted by Admin", resumeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

