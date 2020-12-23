package nsu.ui.mvc;

import javax.validation.Valid;

import nsu.ui.Category;
import nsu.ui.User;
import nsu.ui.UserRepository;
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


@Controller
@RequestMapping("/login")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView createUserForm(@ModelAttribute User user) {
//        user.setUserName("ANDY");
        user.setCurrent(true);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("transactions/login");
//        mav.addObject("userName", user.getUserName());

        return mav;
    }
//    public ModelAndView createTransaction(@Valid Transaction transaction, BindingResult result,
//                                          RedirectAttributes redirect) throws SQLException {
//
//        if (result.hasErrors()) {
//            ArrayList<Category> categories = this.transactionRepository.findCategories();
//            ModelAndView mav = new ModelAndView();
//            mav.setViewName("transactions/create");
//            mav.addObject("categories", categories);
//            mav.addObject("formErrors", result.getAllErrors());
//            return mav;
//        }
    @RequestMapping(value = "/log-error", method = RequestMethod.POST)
    public ModelAndView authUser(@Valid User user, BindingResult result,
                                       RedirectAttributes redirect) throws SQLException {

        System.out.println(result.getAllErrors());
        System.out.println(result.getFieldErrors());
        if (result.hasErrors()) {
//            user.setUserName("ANDY");
            ModelAndView mav = new ModelAndView();
            mav.setViewName("transactions/login");
            mav.addObject("formErrors", result.getAllErrors());
            mav.addObject("isReg", true);
            return mav;
        }

        /*if (result.hasErrors() || this.userRepository.checkDB("users", "user_name", user.getUserName())) {
            ModelAndView mav = new ModelAndView();
            ArrayList<User> users = this.userRepository.findUsers();
            ArrayList<String> names = new ArrayList<>();

            for (User obj:users){
                names.add(obj.getUserName());
            }
            Collections.reverse(names);

            mav.setViewName("transactions/categories");
            mav.addObject("formErrors", result.getAllErrors());
            mav.addObject("categories", names);
            if (!user.getUserName().isEmpty()) {
                mav.addObject("state", "block");
            }

            return mav;
        }*/

        return new ModelAndView("redirect:/transactions");
    }

    @RequestMapping(value = "/reg-error", method = RequestMethod.POST)
    public ModelAndView registerUser(@Valid User user, BindingResult result,
                                   RedirectAttributes redirect) throws SQLException {
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("transactions/login");
            mav.addObject("formErrors", result.getAllErrors());
            mav.addObject("isReg", true);
            return mav;
        }

//        this.userRepository.saveUser(user);

        return new ModelAndView("redirect:/transactions");
    }


    @RequestMapping("foo")
    public String foo() {
        throw new RuntimeException("Expected exception in controller");
    }

}