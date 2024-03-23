package am.itspace.jobboard.controller;

import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resumes")
public class ResumeController {

    private final ResumeService resumeService;
    private final CategoryService categoryService;

    @GetMapping("/{index}")
    public String resumesPage(@PathVariable("index") String indexStr) {
        try {
            int index = Integer.parseInt(indexStr);

            return "job-seeker-list";

        } catch (NumberFormatException e) {
            return "redirect:/resumes/1";
        }
    }

}

