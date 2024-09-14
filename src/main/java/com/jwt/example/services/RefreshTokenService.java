package com.jwt.example.services;

import com.jwt.example.models.RefreshToken;
import com.jwt.example.models.User;
import com.jwt.example.repositories.RefreshTokenRepository;
import com.jwt.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public long refreshTokenValidity=6 * 60 * 60;;

    public RefreshToken createRefreshToken(String userName)
    {
        User user = this.userRepository.findByEmail(userName).get();

        RefreshToken refreshToken1 = user.getRefreshToken();

        if (refreshToken1==null)
        {
             refreshToken1 = RefreshToken.builder().
                    refreshToken(UUID.randomUUID().toString())
                    .expiry(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();
        }
        else
        {
            refreshToken1.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
        }

        user.setRefreshToken(refreshToken1);



        //save to database

        RefreshToken save = this.refreshTokenRepository.save(refreshToken1);

        return refreshToken1;

    }

    public RefreshToken verifyRefreshToken(String refreshToken)
    {

        RefreshToken refreshTokenOb = this.refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()->new RuntimeException("Given token does not exist in db"));



         if (refreshTokenOb.getExpiry().compareTo(Instant.now())<0)
         {
             this.refreshTokenRepository.delete(refreshTokenOb);
             throw  new RuntimeException("Refresh Token expired !!");
         }

         return refreshTokenOb;
    }
}
