package com.jwt.example.services;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryImageService {
    @Autowired
   private Cloudinary cloudinary;

    public Map upload(MultipartFile file)
    {
        try {
            Map upload = cloudinary.uploader().upload(file.getBytes(), Map.of());
            return upload;
        } catch (IOException e) {
            throw new RuntimeException("Image uploading Failed...");
        }

    }

}
