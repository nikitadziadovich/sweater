package com.example.test.controller;

import com.example.test.config.WebSecurityConfig;
import com.example.test.domain.CustomUserDetails;
import com.example.test.domain.User;
import com.example.test.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Map;

@Controller
@SessionAttributes(types = User.class)
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }


    @GetMapping("/main")
    public String main(Map<String, Object> model) {
        model.put("users", userRepository.findAll());
        return "main";
    }

    @PostMapping("block")
    public String block(@RequestParam String filter, Map<String, Object> model) {
        updateStatusByUsername(splitFilter(filter), false);
        if (checkCurrentUserInFilter(splitFilter(filter)))
            return "redirect:/login?logout";
        model.put("users", userRepository.findAll());
        return "main";
    }

    @PostMapping("unblock")
    public String unblock(@RequestParam String filter, Map<String, Object> model) {
        updateStatusByUsername(splitFilter(filter), true);
        model.put("users", userRepository.findAll());
        return "main";
    }

    @PostMapping("delete")
    public String delete(@RequestParam String filter, Map<String, Object> model) {
        deleteByUsername(splitFilter(filter));
        if (checkCurrentUserInFilter(splitFilter(filter)))
            return "redirect:/login?logout";
        model.put("users", userRepository.findAll());
        return "main";
    }

    private void deleteByUsername(String[] filterArray) {
        for (String user : filterArray)
            userRepository.deleteByUsername(user);
    }

    private void updateStatusByUsername(String[] filterArray, Boolean status) {
        for (String user : filterArray)
            userRepository.updateUsrSetStatusForUsername(status, user);
    }

    private Boolean checkCurrentUserInFilter(String[] filterArray) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Arrays.asList(filterArray).contains(authentication.getName()))
            return true;
        return false;
    }

    private String[] splitFilter(String filter) {
        return filter.split(" ");
    }

}
