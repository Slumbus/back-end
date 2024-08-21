package com.firefly.slumbus.music.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.FFmpegResult;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class CombineService {

    @Autowired
    private AmazonS3Client amazonS3;

    public String combineAudioFiles(String musicUrl, File recordedFile) throws IOException {

        // S3 URL에서 버킷 이름과 객체 키 추출
        URL url = new URL(musicUrl);
        String bucketName = url.getHost().split("\\.")[0];
        String musicFileKey = url.getPath().substring(1); // 첫 '/' 제거

        // 디버깅 로그 추가
        System.out.println("Bucket Name: " + bucketName);
        System.out.println("Melody File Key: " + musicFileKey);

        try {
            // S3에서 멜로디 파일 다운로드
            S3Object s3Object = amazonS3.getObject(bucketName, musicFileKey);
            S3ObjectInputStream musicInputStream = s3Object.getObjectContent();

            // 멜로디 파일을 임시 파일로 저장
            File musicFile = File.createTempFile("melody", ".mp3");
            FileOutputStream musicOutputStream = new FileOutputStream(musicFile);
            byte[] readBuf = new byte[1024];
            int readLen;
            while ((readLen = musicInputStream.read(readBuf)) > 0) {
                musicOutputStream.write(readBuf, 0, readLen);
            }
            musicOutputStream.close();
            musicInputStream.close();

            // 합친 파일 생성
            File outputFile = File.createTempFile("combined", ".mp3");

            // FFmpeg 이용해 합치기
            FFmpegResult result = FFmpeg.atPath()
                    .addInput(UrlInput.fromPath(musicFile.toPath()))
                    .addInput(UrlInput.fromPath(recordedFile.toPath()))
                    .addOutput(UrlOutput.toPath(outputFile.toPath()))
                    .setOverwriteOutput(true)
                    .setComplexFilter("[0:a][1:a]amix=inputs=2:duration=longest")
                    .execute();

            // 결과 파일을 S3에 업로드
            String outputKey = "music/" + outputFile.getName();
            amazonS3.putObject(bucketName, outputKey, outputFile);

            // S3 URL 반환
            return amazonS3.getUrl(bucketName, outputKey).toString();

        } catch (AmazonS3Exception e) {
            System.err.println("AmazonS3Exception: " + e.getMessage());
            throw e;
        }

    }

}
