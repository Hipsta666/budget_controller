package nsu.ui.mvc;

import javax.validation.Valid;

import nsu.ui.Category;
import nsu.ui.CategoryRepository;
import nsu.ui.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;



/**
 * @author Rob Winch
 */
@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView createCategoryForm(@ModelAttribute Category category) {
        ArrayList<Category> categories = this.categoryRepository.findCategories();
        ArrayList<String> names = new ArrayList<>();
        for (Category obj:categories){
            names.add(obj.getCategoryName());
        }
        Collections.reverse(names);

        return new ModelAndView("transactions/categories", "categories", names);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView createCategory(@Valid Category category, BindingResult result,
                                       RedirectAttributes redirect) throws SQLException {
        System.out.println(category.getCategoryName());
        if (result.hasErrors() || this.categoryRepository.checkDB("categories", "category_name", category.getCategoryName())) {
            ModelAndView mav = new ModelAndView();
            ArrayList<Category> categories = this.categoryRepository.findCategories();
            ArrayList<String> names = new ArrayList<>();

            for (Category obj:categories){
                names.add(obj.getCategoryName());
            }
            Collections.reverse(names);

            mav.setViewName("transactions/categories");
            mav.addObject("formErrors", result.getAllErrors());
            mav.addObject("categories", names);
            if (!category.getCategoryName().isEmpty()) {
                mav.addObject("state", "block");
            }

            return mav;
        }

        this.categoryRepository.saveCategory(category);
        return new ModelAndView("redirect:/categories","formErrors", result.getAllErrors());

    }

    @RequestMapping(value = "/delete_category/{category}", method = RequestMethod.POST)
    public ModelAndView delete_category(@PathVariable("category") String category,
                                        RedirectAttributes redirect) throws SQLException {

        this.categoryRepository.deleteCategory(category);
        return new ModelAndView("redirect:/categories");
    }


    @RequestMapping("foo")
    public String foo() {
        throw new RuntimeException("Expected exception in controller");
    }

}