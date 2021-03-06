package com.amh.pm.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.amh.pm.entity.Organization;
import com.amh.pm.entity.User;
import com.amh.pm.service.UserService;

@Controller
public class UserController {

    private UserService userService;

    @Autowired(required = true)
    @Qualifier(value = "userService")
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping(value = "/loginuser", method = RequestMethod.POST)
    public String loginUser(@Validated User user, BindingResult result, Model model, HttpServletRequest req) {

        HttpSession session = req.getSession(true);
        String name = user.getName();
        String password = user.getPassword();

        User u = userService.userByName(name, password);
        if (u == null) {

            model.addAttribute("message", "Username or Password Do Not Correct");
            return "redirect:login";
        } else {
            session.setAttribute("userId", u.getId());
            session.setAttribute("name", u.getName());
            session.setAttribute("password", u.getPassword());
        }
        return "redirect:organizations";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("users", this.userService.findAll());
        return "users";
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.GET)
    public String showDetails(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "userDetails";
    }

    @RequestMapping(value = "/registerUser", method = RequestMethod.GET)
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public String addUser(@Validated @ModelAttribute User user, BindingResult result, Model model, HttpServletRequest request) {

        if (result.hasErrors()) {
            return "register";
        } else {

            User existedName = userService.findUserByName(user.getName());
            User existedEmail = userService.findUserByEmail(user.getEmail());

            if ((existedName != null) || (existedEmail != null)) {
                model.addAttribute("user", new User());
                model.addAttribute("existed", "Try another User Name or Email");
                return "register";
            } else {
                userService.save(user);
                return "redirect:login";
            }
        }
    }

}
