package am.itspace.jobboard.controller.admin;

import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.service.UserService;
import lombok.RequiredArgsConstructor;
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
        int totalPages = userService.getTotalPages();
        int index;
        try {
            index = Integer.parseInt(indexStr);
            if (index <= 0 || (index > totalPages && totalPages != 0)) {
                return "redirect:/admin/users/1";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/users/1";
        }

        modelMap.put("page", "users");

        modelMap.put("index", index);
        modelMap.put("searchIndex", 0);
        modelMap.put("totalPages", totalPages);
        modelMap.put("userCount", userService.getUserCount());
        modelMap.put("users", userService.getUsersFromNToM(index));
        return "admin/admin-user-accounts";
    }

    @GetMapping("/search")
    public String getAdminSearchJobPage(@RequestParam(value = "searchIndexStr", defaultValue = "1") String searchIndexStr,
                                        @RequestParam(value = "email") String email,
                                        @RequestParam(value = "role", defaultValue = "") String role, ModelMap modelMap) {

        int totalPagesOfSearchEmailRole = userService.getTotalPagesOfSearch(email,role);
        int searchIndex;

        modelMap.put("page", "users");

        try {
            searchIndex = Integer.parseInt(searchIndexStr);
            if (searchIndex <= 0 || searchIndex > totalPagesOfSearchEmailRole) {
                return "redirect:/admin/users/1";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/users/1";
        }

        modelMap.put("index", 0);
        modelMap.put("searchIndex", searchIndex);
        modelMap.put("email", email);
        try {
            modelMap.put("role", Role.valueOf(role));
        } catch (IllegalArgumentException e) {
            modelMap.put("role", "");
        }
        modelMap.put("totalPages", totalPagesOfSearchEmailRole);
        modelMap.put("userCount", userService.getUserCountOfEmailRole(email,role));
        modelMap.put("users", userService.getUsersFromNToMForSearch(searchIndex, email, role));
        return "admin/admin-user-accounts";
    }
}
