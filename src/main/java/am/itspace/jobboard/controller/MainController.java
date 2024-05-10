package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobWishlist;
import am.itspace.jobboard.security.SpringUser;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.JobWishlistService;
import am.itspace.jobboard.util.PictureUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CategoryService categoryService;
    private final JobService jobService;
    private final JobWishlistService jobWishlistService;
    private final CompanyService companyService;

    @Value("${program.pictures.file.path}")
    private String uploadDirectory;

    @GetMapping("/")
    public String homePage(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        modelMap.addAttribute("firstCategories", categoryService.findTop(6));
        modelMap.addAttribute("categories", categoryService.findTop(9));
        modelMap.addAttribute("jobs", jobService.findTop6());

        List<Company> randomCompanies = companyService.findRandomCompanies(6);

        Map<Company, Integer> companyIntegerHashMap = randomCompanies.stream()
                .collect(Collectors.toMap(
                        company -> company,
                        company -> jobService.getCountByCompanyId(company.getId())
                ));

        modelMap.addAttribute("companies", companyIntegerHashMap);


        if (springUser != null) {
            List<JobWishlist> jobWishlists = jobWishlistService.findAllByUserId(springUser.getUser().getId());
            List<Job> jobList = new ArrayList<>();
            for (JobWishlist jobWishlist : jobWishlists) {
                jobList.add(jobWishlist.getJob());
            }
            modelMap.addAttribute("favoritesJobs", jobList);
        }

        return "index";
    }

    @SneakyThrows
    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("picName") String picName){
        return PictureUtil.getImage(picName, uploadDirectory);
    }
}
