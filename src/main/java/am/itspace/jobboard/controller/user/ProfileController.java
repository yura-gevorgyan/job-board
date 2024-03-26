package am.itspace.jobboard.controller.user;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Gender;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.entity.enums.Status;
import am.itspace.jobboard.entity.enums.WorkExperience;
import am.itspace.jobboard.exception.PasswordNotMuchException;
import am.itspace.jobboard.exception.UseOldPasswordException;
import am.itspace.jobboard.security.SpringUser;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.service.ResumeService;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.util.AddMessageUtil;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final CompanyService companyService;
    private final CategoryService categoryService;
    private final ResumeService resumeService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String employerProfile(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        AddMessageUtil.addMessageToModel(msg, modelMap);
        return "/profile/user-profile";
    }

    @GetMapping("/company")
    public String companyProfilePage(
            @RequestParam(value = "msg", required = false) String msg, ModelMap modelMap,
            @AuthenticationPrincipal SpringUser springUser) {
        AddMessageUtil.addMessageToModel(msg, modelMap);

        if ((springUser != null) && (springUser.getUser().getRole() == Role.COMPANY_OWNER)) {
            modelMap.addAttribute("categories", categoryService.findAll());
            modelMap.addAttribute("company", companyService.findCompanyByUserId(springUser.getUser().getId()));
            return "/profile/company-profile";
        }
        return "redirect:/";
    }

    @GetMapping("/applicant-list")
    public String applicantListCompany() {
        return "/profile/applicant-list";
    }

    @GetMapping("/job-applies")
    public String JobApplies() {
        return "/profile/candidate-shortlisted-jobs";
    }

    @GetMapping("/jobs-create")
    public String createJopPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        AddMessageUtil.addMessageToModel(msg, modelMap);
        modelMap.addAttribute("workExperience", WorkExperience.values());
        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("status", Status.values());
        return "/profile/create-job";
    }

    @GetMapping("/jobs-manage")
    public String jobManage() {
        return "/profile/manage-job";
    }

    @GetMapping("/resume")
    public String createCompanyPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap,
                                    @AuthenticationPrincipal SpringUser springUser) {
        AddMessageUtil.addMessageToModel(msg, modelMap);
        if (springUser != null) {
            modelMap.addAttribute("categories", categoryService.findAll());
            modelMap.addAttribute("gender", Gender.values());
            modelMap.addAttribute("workExperience", WorkExperience.values());
            modelMap.addAttribute("resume", resumeService.findByUserId(springUser.getUser().getId()));
            return "/profile/candidate-profile";
        }
        return "/profile/candidate-profile";
    }

    @GetMapping("/applied-jobs")
    public String companySinglePage() {
        return "/profile/candidate-applied-job";
    }

    @GetMapping("/delete")
    public String deleteAccount(@AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null) {
            springUser.getUser().setDeleted(true);
            return "redirect:/login";
        }
        return "redirect:/";
    }

    @GetMapping("/change-password")
    public String changePasswordPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        AddMessageUtil.addMessageToModel(msg, modelMap);
        return "/profile/candidate-change-password";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam String name,
                                @RequestParam String surname,
                                @AuthenticationPrincipal SpringUser springUser) {
        User user = springUser.getUser();
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
                                 @AuthenticationPrincipal SpringUser springUser) {
        User user = springUser.getUser();

        if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(confirmPassword)) {
            return "redirect:/profile/change-password";
        }

        if (user != null && passwordEncoder.matches(oldPassword, user.getPassword())) {
            try {
                userService.changePassword(newPassword, confirmPassword, user);
                return "redirect:/profile/change-password?msg=Your password is successfully changed";
            } catch (PasswordNotMuchException e) {
                return "redirect:/profile/change-password?msg=Invalid password";
            } catch (UseOldPasswordException e) {
                return "redirect:/profile/change-password?msg=You are using old password";
            }
        }
        return "redirect:/profile/change-password?msg=Invalid old password";
    }
}
