package com.firefly.slumbus.music.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.FFmpegResult;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;
import com.github.kokorin.jaffree.ffprobe.FFprobe;
import com.github.kokorin.jaffree.ffprobe.FFprobeResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CombineService {

    @Autowired
    private AmazonS3Client amazonS3;

    // FFmpeg 명령어를 통해 최대 볼륨을 감지하는 함수
    private double detectMaxVolume(File audioFile) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg",
                "-i", audioFile.getAbsolutePath(),
                "-af", "volumedetect",
                "-f", "null", "-"
        );

        Process process = processBuilder.start();
        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        // FFmpeg의 stderr로부터 출력 로그 읽기
        try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                error.append(line).append("\n");
            }
        }

        // 프로세스 완료 대기
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            System.err.println("FFmpeg volumedetect 오류: " + error);
            throw new RuntimeException("FFmpeg volumedetect 실패");
        }

        // 최대 볼륨을 추출하는 정규식 패턴
        Pattern pattern = Pattern.compile("max_volume: (-?\\d+\\.\\d+) dB");
        Matcher matcher = pattern.matcher(error.toString());

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1)); // 최대 볼륨 값 반환
        }

        // 기본값(0.0) 반환 (감지 실패 시)
        return 0.0;
    }

    public String combineAudioFiles(String musicUrl, File recordedFile) throws IOException, InterruptedException {

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

            // 녹음 파일과 멜로디 파일의 최대 볼륨을 감지
            double melodyMaxVolume = detectMaxVolume(musicFile);
            double recordedMaxVolume = detectMaxVolume(recordedFile);

            // 볼륨 조정 비율 계산 (볼륨이 작은 쪽을 키움)
            double melodyVolumeFactor = 1.0;
            double recordedVolumeFactor = 1.0;

            if (melodyMaxVolume > recordedMaxVolume) {
                recordedVolumeFactor = Math.pow(10, (melodyMaxVolume - recordedMaxVolume) / 20); // 데시벨 차이에 맞춘 볼륨 조정
            } else if (melodyMaxVolume < recordedMaxVolume) {
                melodyVolumeFactor = Math.pow(10, (recordedMaxVolume - melodyMaxVolume) / 20);
            }

            // 합친 파일 생성
            File outputFile = File.createTempFile("combined", ".mp3");

            // FFmpeg 이용해 합치기
            FFmpegResult result = FFmpeg.atPath()
                    .addInput(UrlInput.fromPath(musicFile.toPath()))
                    .addInput(UrlInput.fromPath(recordedFile.toPath()))
                    .addOutput(UrlOutput.toPath(outputFile.toPath()))
                    .setOverwriteOutput(true)
                    .setComplexFilter("[0:a]volume=" + melodyVolumeFactor + "[a0];[1:a]volume=" + recordedVolumeFactor + "[a1];[a0][a1]amix=inputs=2:duration=longest")
                    .execute();

            // 결과 파일을 S3에 업로드
            String outputKey = "music/" + outputFile.getName();
            amazonS3.putObject(bucketName, outputKey, outputFile);

            // S3 URL 반환
            return amazonS3.getUrl(bucketName, outputKey).toString();

        } catch (AmazonS3Exception | InterruptedException e) {
            System.err.println("AmazonS3Exception: " + e.getMessage());
            throw e;
        }

    }

}
