package ra.ss4.controller.B3;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.ss4.entity.B3.FoodItem;
import ra.ss4.entity.B3.FoodStatus;
import ra.ss4.service.B3.FoodItemService;

import java.util.Optional;

@Controller
@RequestMapping("/food-items")
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;

    @GetMapping
    public String listFoodItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<FoodItem> foodItems = foodItemService.searchAndFilter(name, categoryId, status, pageable);

        model.addAttribute("foodItems", foodItems);
        model.addAttribute("categories", foodItemService.findAllCategories());
        model.addAttribute("statuses", FoodStatus.values());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", foodItems.getTotalPages());
        model.addAttribute("totalElements", foodItems.getTotalElements());
        model.addAttribute("searchName", name);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "food-items/list";
    }

    // Hiển thị chi tiết thực phẩm
    @GetMapping("/view/{id}")
    public String viewFoodItem(@PathVariable Long id, Model model) {
        Optional<FoodItem> foodItem = foodItemService.findById(id);
        if (foodItem.isPresent()) {
            model.addAttribute("foodItem", foodItem.get());
            return "food-items/view";
        }
        return "redirect:/food-items";
    }

    // Hiển thị form thêm thực phẩm mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("foodItem", new FoodItem());
        model.addAttribute("categories", foodItemService.findAllCategories());
        model.addAttribute("statuses", FoodStatus.values());
        return "food-items/add";
    }

    // Xử lý thêm thực phẩm mới
    @PostMapping("/add")
    public String addFoodItem(@ModelAttribute @Valid FoodItem foodItem,
                              BindingResult result, Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", foodItemService.findAllCategories());
            model.addAttribute("statuses", FoodStatus.values());
            return "food-items/add";
        }

        try {
            foodItemService.save(foodItem);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Thêm thực phẩm '" + foodItem.getName() + "' thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Có lỗi xảy ra khi thêm thực phẩm: " + e.getMessage());
        }

        return "redirect:/food-items";
    }

    // Hiển thị form cập nhật thực phẩm
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<FoodItem> foodItem = foodItemService.findById(id);
        if (foodItem.isPresent()) {
            model.addAttribute("foodItem", foodItem.get());
            model.addAttribute("categories", foodItemService.findAllCategories());
            model.addAttribute("statuses", FoodStatus.values());
            return "food-items/edit";
        }
        return "redirect:/food-items";
    }

    // Xử lý cập nhật thực phẩm
    @PostMapping("/edit/{id}")
    public String updateFoodItem(@PathVariable Long id,
                                 @ModelAttribute @Valid FoodItem foodItem,
                                 BindingResult result, Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", foodItemService.findAllCategories());
            model.addAttribute("statuses", FoodStatus.values());
            return "food-items/edit";
        }

        try {
            foodItem.setId(id);
            foodItemService.save(foodItem);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Cập nhật thực phẩm '" + foodItem.getName() + "' thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Có lỗi xảy ra khi cập nhật thực phẩm: " + e.getMessage());
        }

        return "redirect:/food-items";
    }

    // Xóa thực phẩm
    @PostMapping("/delete/{id}")
    public String deleteFoodItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<FoodItem> foodItem = foodItemService.findById(id);
            if (foodItem.isPresent()) {
                foodItemService.deleteById(id);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Xóa thực phẩm '" + foodItem.get().getName() + "' thành công!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Có lỗi xảy ra khi xóa thực phẩm: " + e.getMessage());
        }
        return "redirect:/food-items";
    }

    // Xóa nhiều thực phẩm
    @PostMapping("/delete-multiple")
    public String deleteMultipleFoodItems(@RequestParam("ids") Long[] ids,
                                          RedirectAttributes redirectAttributes) {
        try {
            foodItemService.deleteByIds(ids);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Xóa " + ids.length + " thực phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Có lỗi xảy ra khi xóa thực phẩm: " + e.getMessage());
        }
        return "redirect:/food-items";
    }
}
