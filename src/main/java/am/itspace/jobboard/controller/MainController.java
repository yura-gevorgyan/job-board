package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobWishlist;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.security.SpringUser;
import am.itspace.jobboard.service.JobWishlistService;
import am.itspace.jobboard.service.MainService;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.util.PictureUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final JobWishlistService jobWishlistService;
    private final MainService mainService;

    @Value("${program.pictures.file.path}")
    private String uploadDirectory;

    @GetMapping("/")
    public String homePage(ModelMap modelMap,
                           @AuthenticationPrincipal SpringUser springUser,
                           @AuthenticationPrincipal OAuth2User oAuth2User) {

        if (springUser != null || oAuth2User != null) {

            User springUserUser = springUser != null ? springUser.getUser() : null;
            User oauth2User = oAuth2User != null ? userService.findByEmail(oAuth2User.getAttribute("email")) : null;

            mainService.showHomePageDetails(modelMap);

            int userId = Objects.requireNonNullElse(springUserUser, oauth2User).getId();
            List<Job> jobList = jobWishlistService.findAllByUserId(userId).stream()
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
