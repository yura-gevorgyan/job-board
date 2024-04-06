package am.itspace.jobboard.controller;

import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.util.PictureUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CategoryService categoryService;
    private final JobService jobService;

    @Value("${program.pictures.file.path}")
    private String uploadDirectory;

    @GetMapping("/")
    public String homePage(ModelMap modelMap) {
        modelMap.addAttribute("firstCategories", categoryService.findTop(6));
        modelMap.addAttribute("categories", categoryService.findTop(9));
        modelMap.addAttribute("jobs", jobService.findTop6());
        return "index";
    }

    @SneakyThrows
    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("picName") String picName){
        return PictureUtil.getImage(picName, uploadDirectory);
    }
}
