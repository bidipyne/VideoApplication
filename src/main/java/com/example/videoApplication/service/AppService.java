package com.example.videoApplication.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.videoApplication.model.User;
import com.example.videoApplication.repository.IAppRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

@Service
public class AppService {
    @Autowired
    IAppRepo appRepo;

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${s3.bucket.name}")
    private String s3BucketName;

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            System.out.println("Error {} occurred while converting the multipart file "+ e.getLocalizedMessage());
        }
        return file;
    }

    public void saveVideo(final MultipartFile multipartFile) {
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            final String fileName = LocalDateTime.now() + "_" + file.getName();
            System.out.println("Uploading file with name "+ fileName);
            final PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, fileName, file);
            amazonS3.putObject(putObjectRequest);
            Files.delete(file.toPath()); // Remove the file locally created in the project folder
        } catch (AmazonServiceException e) {
            System.out.println("Error {} occurred while uploading file"+ e.getLocalizedMessage());
        } catch (IOException ex) {
            System.out.println("Error {} occurred while deleting temporary file"+ ex.getLocalizedMessage());
        }
    }


    public boolean saveOrUpdateUserInfo(User user){
        try{
            appRepo.save(user);
            return true;
        }
        catch (Exception ex){
            System.out.println("Something went wrong! "+ex);
            return false;
        }
    }
}
