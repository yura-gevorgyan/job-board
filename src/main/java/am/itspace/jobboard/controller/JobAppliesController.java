package am.itspace.jobboard.controller;

import am.itspace.jobboard.service.JobAppliesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/job/applies")
public class JobAppliesController {

    private final JobAppliesService jobAppliesService;


}

