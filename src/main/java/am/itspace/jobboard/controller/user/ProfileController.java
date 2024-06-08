package am.itspace.jobboard.controller.user;

import am.itspace.jobboard.config.PasswordProperties;
import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Gender;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.entity.enums.Status;
import am.itspace.jobboard.entity.enums.WorkExperience;
import am.itspace.jobboard.exception.PasswordNotMuchException;
import am.itspace.jobboard.exception.PasswordToShortException;
import am.itspace.jobboard.exception.UseOldPasswordException;
import am.itspace.jobboard.security.SecurityService;
import am.itspace.jobboard.service.*;
import am.itspace.jobboard.util.AddErrorMessageUtil;
import am.itspace.jobboard.util.GenerateTokenUtil;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final CompanyService companyService;
    private final CategoryService categoryService;
    private final ResumeService resumeService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordProperties passwordProperties;
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
    public String deleteAccountPage() {
        User user = securityService.getCurrentUser();
        if (user != null) {
            if (!user.getPassword().equals(passwordProperties.getOAuth2UserPassword())) {
                return "/profile/delete-profile";
            } else {
                userService.deleteProfileCode(user);
                return "/profile/delete-profile-confirm";
            }
        }
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteAccount(@RequestParam String password,
                                RedirectAttributes redirectAttributes) {
        User user = securityService.getCurrentUser();

        if (!password.isBlank()) {
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                try {
                    userService.deleteProfileCode(user);
                    return "/profile/delete-profile-confirm";
                } catch (PasswordNotMuchException e) {
                    redirectAttributes.addFlashAttribute("msg", "Invalid password");
                    return "redirect:/profile/delete";
                }
            }
        }

        redirectAttributes.addFlashAttribute("msg", "Invalid password");
        return "redirect:/profile/delete";
    }

    @PostMapping("/delete/confirm")
    public String deleteProfileConfirm(@RequestParam String confirmEmailCode,
                                       RedirectAttributes redirectAttributes) {

        if (confirmEmailCode == null || confirmEmailCode.isBlank()) {
            return "redirect:/profile/delete";
        }
        if (userService.confirmEmailForDelete(confirmEmailCode) != null) {
            User user = securityService.getCurrentUser();
            userService.deleteProfile(user);
            log.info("User has been confirmed with the confirm code of {}", confirmEmailCode);
            log.info("User with {} id is deleted profile", user.getId());
            return "redirect:/logout";
        }
        redirectAttributes.addFlashAttribute("msg", "Invalid confirm code.");
        return "/profile/delete-profile-confirm";

    }

    @GetMapping("/change-password")
    public String changePasswordPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        User user = securityService.getCurrentUser();
        if (user.getPassword().equals(passwordProperties.getOAuth2UserPassword())) {
            modelMap.addAttribute("cannotChangePasswordMsg", "You cannot change the password because you are logged in with social media.");
        }
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
            log.info("User by {} id and {} username, has updated the name or surname", user.getId(), user.getEmail());
        }
        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes redirectAttributes) {
        User user = securityService.getCurrentUser();

        if (user.getPassword().equals(passwordProperties.getOAuth2UserPassword())) {
            redirectAttributes.addFlashAttribute("msg", "You cannot change the password because you are logged in with social media.");
            return "redirect:/profile/change-password";
        }

        if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(confirmPassword)) {
            return "redirect:/profile/change-password";
        }

        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            try {
                userService.changePassword(newPassword, confirmPassword, user);
                redirectAttributes.addFlashAttribute("msg", "Your password is successfully changed.");
                log.info("User by {} id and {} username, has changed account password", user.getId(), user.getEmail());
                return "redirect:/profile/change-password";

            } catch (PasswordNotMuchException e) {
                redirectAttributes.addFlashAttribute("msg", "Invalid password.");
                return "redirect:/profile/change-password";

            } catch (UseOldPasswordException e) {
                redirectAttributes.addFlashAttribute("msg", "You are using old password.");
                return "redirect:/profile/change-password";

            } catch (PasswordToShortException e) {
                redirectAttributes.addFlashAttribute("msg", "Password must be at least 8 characters long.");
                return "redirect:/profile/change-password";
            }
        }
        redirectAttributes.addFlashAttribute("msg", "Invalid old password.");
        return "redirect:/profile/change-password";
    }
}
