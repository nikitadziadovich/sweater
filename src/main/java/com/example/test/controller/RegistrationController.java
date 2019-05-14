package com.example.test.controller;

import com.example.test.domain.User;
import com.example.test.domain.Role;
import com.example.test.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        if (checkUserExsists(user)) {
            model.put("message", "User exists!");
            return "registration";
        }
        addNewUserToDb(user);
        return "redirect:/login";
    }

    private Boolean checkUserExsists(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        return userFromDb != null;
    }

    private void addNewUserToDb(User user) {
        user.setStatus(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
    }
}
