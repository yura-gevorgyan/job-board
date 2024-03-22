package am.itspace.jobboard.controller;

import am.itspace.jobboard.service.ApplicantListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/applicant/list")
public class ApplicantListController {

    private ApplicantListService applicantListService;

    @GetMapping
    public String applicantListCompany() {
        return "/profile/applicant-list";
    }
}

