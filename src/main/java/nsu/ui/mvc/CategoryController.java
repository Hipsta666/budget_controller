package nsu.ui.mvc;

import javax.validation.Valid;

import nsu.ui.Category;
import nsu.ui.CategoryRepository;
import nsu.ui.Transaction;
import nsu.ui.User;
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
    private User user;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @RequestMapping(value = "/{userLogin}", method = RequestMethod.GET)
    public ModelAndView createCategoryForm(@ModelAttribute Category category, @PathVariable("userLogin") String userLogin) {
        this.user = this.categoryRepository.findUser(userLogin);

        this.categoryRepository.setGlobalUserId(user.getId());
        System.out.println(111);
        System.out.println(user.getId());
        System.out.println(userLogin);

        System.out.println(user.getId());
        System.out.println(222);
        ArrayList<Category> categories = this.categoryRepository.findCategories();
        ArrayList<String> names = new ArrayList<>();
        for (Category obj:categories){
            System.out.println(obj.getUser_id());
            names.add(obj.getCategoryName());
        }
        Collections.reverse(names);




        category.setUser_id(user.getId());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("transactions/categories");
        mav.addObject("user", user);
        mav.addObject("categories", names);
        return mav;
    }

    @RequestMapping(value = "/{userLogin}", method = RequestMethod.POST)
    public ModelAndView createCategory(@PathVariable("userLogin") String userLogin, @Valid Category category, BindingResult result,
                                       RedirectAttributes redirect) throws SQLException {
        this.user = this.categoryRepository.findUser(userLogin);

        this.categoryRepository.setGlobalUserId(category.getUser_id());
        category.setUser_id(user.getId());
        if (result.hasErrors() || this.categoryRepository.checkDBint(category.getCategoryName(), user.getId().intValue())) {
            ModelAndView mav = new ModelAndView();

            this.categoryRepository.setGlobalUserId(category.getUser_id());
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
        System.out.println(123);
        System.out.println(category.getUser_id());
        System.out.println(456);


        this.categoryRepository.saveCategory(category);
        return new ModelAndView("redirect:/categories/" + userLogin);

    }

    @RequestMapping(value = "/delete_category/{category}/{userLogin}", method = RequestMethod.POST)
    public ModelAndView delete_category(@PathVariable("category") String category, @PathVariable("userLogin") String userLogin,
                                        RedirectAttributes redirect) throws SQLException {
        this.user = this.categoryRepository.findUser(userLogin);
        this.categoryRepository.setGlobalUserId(user.getId());
        this.categoryRepository.deleteCategory(category);
        return new ModelAndView("redirect:/categories/" + userLogin);
    }


    @RequestMapping("foo")
    public String foo() {
        throw new RuntimeException("Expected exception in controller");
    }

}