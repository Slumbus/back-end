package com.firefly.slumbus.base.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.firefly.slumbus.base.exception.InternalServerException;
import com.firefly.slumbus.base.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.firefly.slumbus.base.code.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String uploadImage(MultipartFile image) {

        String fileName = createFileName("images/", image.getOriginalFilename());
        String fileUrl = amazonS3.getUrl(bucket, fileName).toString();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(image.getSize());
        objectMetadata.setContentType(image.getContentType());

        try {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, image.getInputStream(), objectMetadata));
//                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(IOException e) {
            throw new InternalServerException(S3_IMAGE_UPLOAD_ERROR);
        }

        return fileUrl;
    }

    public String uploadMusic(MultipartFile music) {

        String fileName = createFileName("music/", music.getOriginalFilename());
        String fileUrl = amazonS3.getUrl(bucket, fileName).toString();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(music.getSize());
        objectMetadata.setContentType(music.getContentType());

        try {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, music.getInputStream(), objectMetadata));
        } catch(IOException e) {
            throw new InternalServerException(S3_MUSIC_UPLOAD_ERROR);
        }

        return fileUrl;
    }

    public List<String> uploadImages(List<MultipartFile> images) {

        List<String> fileNameList = new ArrayList<>();
        List<String> fileUrlList = new ArrayList<>();

        images.forEach(file -> {
            String fileName = createFileName("images/", file.getOriginalFilename());
            String fileUrl = amazonS3.getUrl(bucket, fileName).toString();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(String.valueOf(file.getSize()));
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata));
                        //.withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                log.error(e.getMessage());
                throw new InternalServerException(S3_IMAGE_UPLOAD_ERROR);
            }

            fileNameList.add(fileName);
            fileUrlList.add(fileUrl);
        });

        return fileUrlList;
    }

    public List<String> uploadMusics(List<MultipartFile> musics) {

        List<String> fileNameList = new ArrayList<>();
        List<String> fileUrlList = new ArrayList<>();

        musics.forEach(file -> {
            String fileName = createFileName("musics/", file.getOriginalFilename());
            String fileUrl = amazonS3.getUrl(bucket, fileName).toString();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(String.valueOf(file.getSize()));
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata));
            } catch(IOException e) {
                log.error(e.getMessage());
                throw new InternalServerException(S3_IMAGE_UPLOAD_ERROR);
            }

            fileNameList.add(fileName);
            fileUrlList.add(fileUrl);
        });

        return fileUrlList;
    }

    public void deleteImage(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, "images/" + fileName));
    }

    public void deleteMusic(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, "music/" + fileName));
    }

    public String createFileName(String folder, String fileName) {
        return folder + UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch(StringIndexOutOfBoundsException e) {
            throw new InvalidValueException(INVALID_IMAGE_TYPE);
        }
    }

}
