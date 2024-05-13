package am.itspace.jobboard.controller.admin;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserAccountController {

    private final UserService userService;

    @GetMapping("/{indexStr}")
    public String getUsersPage(@PathVariable String indexStr, ModelMap modelMap) {
        try {
            if (indexStr == null || indexStr.isEmpty()) {
                return "redirect:/admin/users/1";
            }
            int index = Integer.parseInt(indexStr);
            if (index <= 0) {
                return "redirect:/admin/users/1";
            }

            Page<User> users = userService.findAllUsers(index);

            if (index > users.getTotalPages() && users.getTotalPages() != 0) {
                return "redirect:/admin/users/1";
            }

            modelMap.put("page", "users");

            modelMap.put("index", index);
            modelMap.put("searchIndex", 0);
            modelMap.put("totalPages", users.getTotalPages());
            modelMap.put("userCount", users.getNumberOfElements());
            modelMap.put("users", users);
        } catch (Exception e) {
            return "redirect:/admin/users/1";
        }
        return "admin/admin-user-accounts";
    }

    @GetMapping("/search")
    public String getAdminSearchJobPage(@RequestParam(value = "searchIndexStr", defaultValue = "1") String searchIndexStr,
                                        @RequestParam(value = "email", defaultValue = "") String email,
                                        @RequestParam(value = "role", defaultValue = "") String role,
                                        ModelMap modelMap) {
        try {
            int searchIndex = Integer.parseInt(searchIndexStr);
            if (searchIndex <= 0) {
                throw new Exception();
            }
            Role currentRole = null;
            try {
                currentRole = Role.valueOf(role);
                modelMap.put("role", currentRole.name());
            } catch (IllegalArgumentException e) {
                modelMap.put("role", "");
            }

            Specification<User> userSpecification = UserSpecification.searchUsers(null, null, email, currentRole);
            Page<User> users = userService.findAllUsers(userSpecification, searchIndex);

            if (searchIndex > users.getTotalPages() && users.getTotalPages() != 0) {
                return "redirect:/admin/users/1";
            }

            modelMap.put("page", "users");

            modelMap.put("index", 0);
            modelMap.put("searchIndex", searchIndex);
            modelMap.put("email", email);
            modelMap.put("totalPages", users.getTotalPages());
            modelMap.put("userCount", users.getNumberOfElements());
            modelMap.put("users", users);

            return "admin/admin-user-accounts";
        } catch (Exception e) {
            return "redirect:/admin/users/1";
        }
    }
}
