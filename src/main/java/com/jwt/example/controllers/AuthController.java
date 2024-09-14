package com.jwt.example.controllers;

import com.jwt.example.helper.JwtHelper;
import com.jwt.example.models.*;
import com.jwt.example.services.RefreshTokenService;
import com.jwt.example.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;


    @Autowired
    private JwtHelper helper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {



        this.doAuthenticate(request.getEmail(), request.getPassword());


        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);
        RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(userDetails.getUsername());


        JwtResponse response = JwtResponse.builder()
                .token(token)
                .refreshToken(refreshToken.getRefreshToken())
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {



        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,password);


        System.out.println(authentication);
        try {

           this.manager.authenticate(authentication);



        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

    @PostMapping("/create-user")
    public User createUser(@RequestBody User user)
    {
        String encode = this.passwordEncoder.encode(user.getPassword());
           user.setPassword(encode);
        return this.userService.createUser(user);
    }


    @PostMapping("/refresh")
    public JwtResponse refreshJwtToken(@RequestBody RefreshTokenRequest refreshToken)
    {
        RefreshToken refreshToken1 = this.refreshTokenService.verifyRefreshToken(refreshToken.getRefreshToken());

        User user = refreshToken1.getUser();
        String token = this.helper.generateToken(user);

        return JwtResponse.builder().refreshToken(refreshToken.getRefreshToken())
                .token(token)
                .username(user.getEmail())
                .build();

    }


}
