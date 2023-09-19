package com.green.greenearthforus.image.controller;

import com.green.greenearthforus.image.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService){
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> imageUpload(@RequestPart(name = "image") MultipartFile image){
        String imageUrl = imageService.uploadImage(image);

        return new ResponseEntity<>(imageUrl, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<Void> imageDelete(@RequestPart(name = "image") String imageUrl){
        imageService.deleteImage(imageUrl);
        return ResponseEntity.noContent().build();
    }


}
