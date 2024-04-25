package am.itspace.jobboard.controller.endpoint;


import am.itspace.jobboard.service.ApplicantListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/applicant-list")
public class ApplicantListEndpoint {

    private final ApplicantListService applicantListService;

    @DeleteMapping("/delete/{applicantListId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteApplicantList(@PathVariable int applicantListId) {
        applicantListService.deleteById(applicantListId);
    }

    @PostMapping("/approve/{applicantListId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void approveJobApply(@PathVariable("applicantListId") int id) {
        applicantListService.changeStatusApproved(id);
    }

    @PostMapping("/reject/{applicantListId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rejectJobApply(@PathVariable("applicantListId") int id) {
        applicantListService.changeStatusRejected(id);
    }
}
