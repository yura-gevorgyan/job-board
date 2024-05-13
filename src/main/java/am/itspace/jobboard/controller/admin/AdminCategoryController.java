package am.itspace.jobboard.controller.admin;

import am.itspace.jobboard.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String getCategoriesPage(@RequestParam(value = "error", required = false) String error,
                                    ModelMap modelMap) {
        if (error != null) {
            modelMap.put("error", error);
        }
        modelMap.put("page", "categories");

        modelMap.put("categoryCount", categoryService.getCategoryCount());
        modelMap.put("categories", categoryService.findAll());
        return "admin/admin-category-config";
    }

    @PostMapping("/add")
    public String addCategory(@RequestParam("pic_name") MultipartFile multipartFile,
                              @RequestParam("name") String name,
                              RedirectAttributes redirectAttributes) {
        if (name == null || name.trim().isEmpty() || categoryService.existsByName(name)) {
            String error = "Wrong name of category, or name already exists.";
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/admin/categories";
        }
        if (multipartFile == null || multipartFile.isEmpty() || multipartFile.getSize() < 1) {
            String error = "Wrong picture of category.";
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/admin/categories";
        }
        categoryService.save(name, multipartFile);
        return "redirect:/admin/categories";
    }

    @GetMapping("/{idStr}")
    public String updateCategory(@PathVariable("idStr") String idStr,
                                 ModelMap modelMap) {
        int categoryId;
        try {
            if (idStr == null) {
                return "redirect:/admin/categories";
            }
            categoryId = Integer.parseInt(idStr);
            if (categoryService.findById(categoryId) == null)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            return "redirect:/admin/categories";
        }
        modelMap.put("page", "categories");

        modelMap.put("categoryCount", categoryService.getCategoryCount());
        modelMap.put("categories", categoryService.findAll());
        modelMap.put("category", categoryService.findById(categoryId));
        return "admin/admin-category-single";
    }

    @PostMapping("/update")
    public String updateCategory(@RequestParam("pic_name") MultipartFile multipartFile,
                                 @RequestParam("name") String name,
                                 @RequestParam("id") String idStr,
                                 RedirectAttributes redirectAttributes) {
        int id;
        if (name == null || name.trim().isEmpty() || idStr == null || idStr.trim().isEmpty()) {
            String error = "Wrong name of category, or name already exists.";
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/admin/categories";
        }
        try {
            id = Integer.parseInt(idStr);
            if (categoryService.findById(id) == null) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            String error = "Category by this id, doest exist.";
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/admin/categories";
        }
        categoryService.update(id, name, multipartFile);
        return "redirect:/admin/categories";
    }
}
