package am.itspace.jobboard.controller.user;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Gender;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.entity.enums.Status;
import am.itspace.jobboard.entity.enums.WorkExperience;
import am.itspace.jobboard.exception.PasswordNotMuchException;
import am.itspace.jobboard.exception.UseOldPasswordException;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.CompanyPictureService;
import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.service.CountryService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.ResumeService;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.util.AddErrorMessageUtil;
import am.itspace.jobboard.security.SecurityService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final CompanyService companyService;
    private final CategoryService categoryService;
    private final ResumeService resumeService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CompanyPictureService companyPictureService;
    private final JobService jobService;
    private final CountryService countryService;
    private final SecurityService securityService;

    @GetMapping
    public String employerProfile() {
        return "/profile/user-profile";
    }

    @GetMapping("/company")
    public String companyProfilePage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        User user = securityService.getCurrentUser();
        AddErrorMessageUtil.addMessageToModel(msg, modelMap);

        if ((user != null) && (user.getRole() == Role.COMPANY_OWNER)) {
            Company companyByUserIdAndIsActiveTrue = companyService.findCompanyByUserIdAndIsActiveTrue(user.getId());
            modelMap.addAttribute("categories", categoryService.findAll());
            modelMap.addAttribute("company", companyByUserIdAndIsActiveTrue);
            modelMap.addAttribute("countries", countryService.findAll());
            if (companyByUserIdAndIsActiveTrue != null) {
                modelMap.addAttribute("companyPictures", companyPictureService.findAllByCompanyId(companyByUserIdAndIsActiveTrue.getId()));
            }
            return "/profile/company-profile";
        }
        return "redirect:/";
    }

    @GetMapping("/applicant-list")
    public String applicantListCompany() {
        return "/profile/applicant-list";
    }

    @GetMapping("/jobs-create")
    public String createJopPage(@RequestParam(value = "msg", required = false) String msg,
                                @RequestParam(value = "update", required = false) String idStr,
                                ModelMap modelMap) {
        User user = securityService.getCurrentUser();

        if (msg != null && !msg.isBlank()) {
            modelMap.addAttribute("msg", msg);
        }

        if (idStr != null && !idStr.isBlank()) {
            try {
                Job byId = jobService.findById(Integer.parseInt(idStr));
                if (byId == null || byId.getUser().getId() != user.getId()) {
                    throw new Exception();
                }
                modelMap.addAttribute("job", byId);
            } catch (Exception e) {
                return "redirect:/profile/jobs-create";
            }
        }

        modelMap.addAttribute("countries", countryService.findAll());
        modelMap.addAttribute("workExperience", WorkExperience.values());
        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("status", Status.values());

        return "/profile/create-job";
    }

    @GetMapping("/jobs-manage/{index}")
    public String jobManage(@PathVariable("index") String indexStr, ModelMap modelMap) {
        User user = securityService.getCurrentUser();

        try {

            if (indexStr == null || indexStr.isEmpty()) {
                return "redirect:/profile/jobs-manage/1";
            }

            int index = Integer.parseInt(indexStr);
            if (index <= 0) {
                return "redirect:/profile/jobs-manage/1";
            }

            Page<Job> jobs = jobService.findAllByUserId(index, user.getId());

            modelMap.put("jobs", jobs);
            modelMap.put("categories", categoryService.findAll());
            modelMap.put("totalPages", jobs.getTotalPages());
            modelMap.put("jobCount", jobs.getNumberOfElements());
            modelMap.put("index", index);
            modelMap.put("searchIndex", 0);

            if (index > jobs.getTotalPages() && jobs.getTotalPages() != 0) {
                return "redirect:/profile/jobs-manage/1";
            }
            return "/profile/manage-job";

        } catch (NumberFormatException e) {
            return "redirect:/profile/jobs-manage/1";
        }
    }

    @GetMapping("/resume")
    public String createCompanyPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        User user = securityService.getCurrentUser();
        AddErrorMessageUtil.addMessageToModel(msg, modelMap);

        if (user != null) {
            modelMap.addAttribute("categories", categoryService.findAll());
            modelMap.addAttribute("gender", Gender.values());
            modelMap.addAttribute("workExperience", WorkExperience.values());
            modelMap.addAttribute("countries", countryService.findAll());
            modelMap.addAttribute("resume", resumeService.findByUserIdAndIsActiveTrue(user.getId()));
            return "/profile/candidate-profile";
        }
        return "/profile/candidate-profile";
    }

    @GetMapping("/delete")
    public String deleteAccount() {
        User user = securityService.getCurrentUser();
        if (user != null) {
            user.setDeleted(true);
            return "redirect:/login";
        }
        return "redirect:/";
    }

    @GetMapping("/change-password")
    public String changePasswordPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        AddErrorMessageUtil.addMessageToModel(msg, modelMap);
        return "/profile/candidate-change-password";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam String name, @RequestParam String surname) {
        User user = securityService.getCurrentUser();
        if (user != null && (!user.getName().equals(name) || !user.getSurname().equals(surname))) {
            user.setName(name);
            user.setSurname(surname);
            userService.update(user);
        }
        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes redirectAttributes) {
        User user = securityService.getCurrentUser();

        if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(confirmPassword)) {
            return "redirect:/profile/change-password";
        }

        if (user != null && passwordEncoder.matches(oldPassword, user.getPassword())) {

            try {
                userService.changePassword(newPassword, confirmPassword, user);
                redirectAttributes.addFlashAttribute("msg", "Your password is successfully changed.");
                return "redirect:/profile/change-password";

            } catch (PasswordNotMuchException e) {
                redirectAttributes.addFlashAttribute("msg", "Invalid password.");
                return "redirect:/profile/change-password";

            } catch (UseOldPasswordException e) {
                redirectAttributes.addFlashAttribute("msg", "You are using old password.");
                return "redirect:/profile/change-password";
            }
        }
        redirectAttributes.addFlashAttribute("msg", "Invalid old password.");
        return "redirect:/profile/change-password";
    }
}
