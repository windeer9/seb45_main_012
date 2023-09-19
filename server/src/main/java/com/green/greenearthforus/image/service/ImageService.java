package com.green.greenearthforus.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.util.UUID;

@Service
public class ImageService {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public ImageService(AmazonS3 amazonS3){
        this.amazonS3 = amazonS3;
    }


    @SneakyThrows
    public String uploadImage(MultipartFile file){
        String filePath = "images/";
        String fileName = filePath + UUID.randomUUID() + "-" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        InputStream inputStream = file.getInputStream();

        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));

        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public void deleteImage(String imageUrl){
//
//        URL url = new URL(imageUrl);
//        String name = bucketName+".s3.ap-northeast-2.amazonaws.com";
//        String path = url.getPath();
//
//        String filePath = path.substring(name.length()+2);

        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, imageUrl));
    }

}
