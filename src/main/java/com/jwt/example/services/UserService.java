package com.jwt.example.services;

import com.jwt.example.models.User;
import com.jwt.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;


    public List<User> getAllUsers()
    {
        List<User> all = this.userRepository.findAll();
        return all;
    }

    public User getUser(int userId)
    {
        User user = this.userRepository.findById(userId).get();
        return user;
    }

    public User createUser(User user)
    {

        User save = this.userRepository.save(user);
        return save;
    }

}
