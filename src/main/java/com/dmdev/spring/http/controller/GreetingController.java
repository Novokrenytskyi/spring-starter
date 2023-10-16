package com.dmdev.spring.http.controller;

import com.dmdev.spring.database.entity.Role;
import com.dmdev.spring.dto.UserReadDto;
import liquibase.pro.packaged.S;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1")
@SessionAttributes("user")
public class GreetingController {

    @ModelAttribute("roles")
    public List<Role> roles() {
        return Arrays.asList(Role.values());
    }

    @GetMapping(value = "/hello")
    public String hello(Model model, HttpServletRequest request, UserReadDto userReadDto) {

        model.addAttribute("user", userReadDto);

        return "greeting/hello";
    }

    @GetMapping(value = "/bye")
    public String bye(ModelAndView modelAndView, @SessionAttribute("user") UserReadDto user) {

        return "greeting/bye";
    }

    @GetMapping(value = "/hello{id}")
    public ModelAndView hello2(ModelAndView modelAndView, HttpServletRequest request,
                              @RequestParam Integer age,
                              @RequestHeader String accept,
                              @CookieValue("JSESSIONID") String jsessionId,
                              @PathVariable("id") Integer id) {

        String ageParamValue = request.getParameter("age");
        String acceptHeader = request.getHeader("accept");
        Cookie[] cookies = request.getCookies();

        modelAndView.setViewName("greeting/hello");
        return modelAndView;
    }

}
