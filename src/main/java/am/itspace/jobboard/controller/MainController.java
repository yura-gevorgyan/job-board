package am.itspace.jobboard.controller;

import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CategoryService categoryService;
    private final JobService jobService;

    @GetMapping("/")
    public String homePage(ModelMap modelMap) {
        modelMap.addAttribute("categories", categoryService.findTop9());
        modelMap.addAttribute("jobs", jobService.findTop6());
        return "index";
    }
}

