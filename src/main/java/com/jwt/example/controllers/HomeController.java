package com.jwt.example.controllers;

import com.jwt.example.models.User;
import com.jwt.example.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class HomeController {

    Logger logger= LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserService userService;


    @GetMapping("/user")
    public List<User> getUser()
    {

        List<User> allUsers = this.userService.getAllUsers();

        return allUsers;
    }

    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable("userId") int userId)
    {
        User user = this.userService.getUser(userId);
        return user;
    }

    @GetMapping("/current-user")
    public String getLoggedInUser(Principal principal)
    {
        String name = principal.getName();
        return name;
    }
}
