package com.example.eventmanager.controller;

import com.example.eventmanager.dao.UsersRepository;
import com.example.eventmanager.domain.User;
import com.example.eventmanager.service.SecurityService;
import com.example.eventmanager.service.UserService;
import com.example.eventmanager.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class HelloController {

    private static final String REDIRECT_TO_ROOT = "redirect:/";

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    SecurityService securityService;

    @Autowired
    UserService userService;

    @Autowired
    UserValidator userValidator;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView start() {
        return new ModelAndView("home");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("user", new User());
            return modelAndView;
        } else {
            return new ModelAndView(REDIRECT_TO_ROOT);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(User user) {
        try {
            securityService.login(user.getUsername(), user.getPassword());
            return new ModelAndView(REDIRECT_TO_ROOT);
        } catch (AuthException e) {
            return new ModelAndView("login");
        }
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            ModelAndView modelAndView = new ModelAndView("registration");
            modelAndView.addObject("userRegistration", new User());
            return modelAndView;
        } else {
            return new ModelAndView(REDIRECT_TO_ROOT);
        }
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@ModelAttribute("userRegistration") User userRegistration, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        userValidator.validate(userRegistration, bindingResult);
        if(!bindingResult.hasErrors()) {
            userService.saveUser(userRegistration.copy());
            try {
                securityService.login(userRegistration.getUsername(), userRegistration.getPassword());
                return new ModelAndView(REDIRECT_TO_ROOT);
            } catch (AuthException ignored) {
                return new ModelAndView(REDIRECT_TO_ROOT);
            }
        } else {
            modelAndView.addObject("userRegistration", userRegistration);
            modelAndView.setViewName("registration");
            return modelAndView;
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return new ModelAndView(REDIRECT_TO_ROOT);
    }

}
