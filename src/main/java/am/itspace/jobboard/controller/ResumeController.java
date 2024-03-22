package am.itspace.jobboard.controller;

import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resume")
public class ResumeController {

    private final ResumeService resumeService;
    private final CategoryService categoryService;

}

