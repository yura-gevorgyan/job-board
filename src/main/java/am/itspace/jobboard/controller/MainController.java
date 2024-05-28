package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobWishlist;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.security.SecurityService;
import am.itspace.jobboard.service.JobWishlistService;
import am.itspace.jobboard.service.MainService;
import am.itspace.jobboard.service.UserService;
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

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final JobWishlistService jobWishlistService;
    private final MainService mainService;
    private final SecurityService securityService;

    @Value("${program.pictures.file.path}")
    private String uploadDirectory;

    @GetMapping("/")
    public String homePage(ModelMap modelMap) {

        User user = securityService.getCurrentUser();

        if (user != null) {
            mainService.showHomePageDetails(modelMap);
            List<Job> jobList = jobWishlistService.findAllByUserId(user.getId()).stream()
                    .map(JobWishlist::getJob)
                    .collect(Collectors.toList());
            modelMap.addAttribute("favoritesJobs", jobList);
            return "index";
        }
        mainService.showHomePageDetails(modelMap);
        return "index";
    }

    @SneakyThrows
    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("picName") String picName) {
        return PictureUtil.getImage(picName, uploadDirectory);
    }
}
