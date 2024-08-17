package com.firefly.slumbus.music.controller;

import com.firefly.slumbus.base.config.S3Service;
import com.firefly.slumbus.music.dto.MusicResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/song")
public class MusicMergeController {

    private final S3Service s3Service;

    // 오디오 파일들 임시저장할 절대경로 설정
    private static final String UPLOAD_DIR = "C:\\Slumbus\\uploads/";

    @PostMapping("/combine")
    public ResponseEntity<String> combineMusic(@RequestParam("melody") String musicUrl, @RequestPart("recording") MultipartFile recordedFile) {
        try {
            // URL에서 오디오 파일을 다운로드하여 저장
            String melodyPath = downloadFileFromUrl(musicUrl);
            log.info("멜로디 파일 저장");

            // 업로드된 녹음 파일 저장
            String recordedFilePath = saveFile(recordedFile);
            log.info("녹음 파일 저장");

            // 오디오 파일 결합
            String combinedFilePath = mergeAudioFiles(melodyPath, recordedFilePath);
            log.info("오디오 파일 결합");
            File combinedFile = new File(combinedFilePath);

            // 결합된 파일을 S3에 업로드
            String s3Url = s3Service.uploadFile(combinedFile);

            // 로컬에 저장된 파일 삭제
            deleteLocalFiles(melodyPath, recordedFilePath, combinedFilePath);
            log.info("로컬에 저장된 파일 삭제");

            // S3 URL을 클라이언트로 응답
            return ResponseEntity.ok("파일이 성공적으로 결합되고 S3에 저장되었습니다. S3 URL: " + s3Url);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("파일 처리 오류");
        }
    }

    private String downloadFileFromUrl(String fileUrl) throws IOException {
        String fileName = UUID.randomUUID() + "_" + Paths.get(new URL(fileUrl).getPath()).getFileName().toString();
        String filePath = UPLOAD_DIR + fileName;

        // 디렉토리가 존재하지 않으면 생성
        Files.createDirectories(Paths.get(UPLOAD_DIR));

        // URL에서 파일 다운로드
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            StreamUtils.copy(new URL(fileUrl).openStream(), outputStream);
        }

        return filePath;
    }

    private String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = UPLOAD_DIR + fileName;

        Files.createDirectories(Paths.get(UPLOAD_DIR));
        file.transferTo(new File(filePath));

        return filePath;
    }

    private String mergeAudioFiles(String originalAudioPath, String recordedAudioPath) throws IOException {
        String outputFilePath = UPLOAD_DIR + "combined_" + UUID.randomUUID() + ".mp3";

        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg", "-i", originalAudioPath, "-i", recordedAudioPath, "-filter_complex",
                "[0:a][1:a]amerge=inputs=2[a]", "-map", "[a]", "-ac", "2", outputFilePath
        );

        Process process = processBuilder.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("오디오 결합 중 인터럽트 발생", e);
        }

        return outputFilePath;
    }

    private void deleteLocalFiles(String... filePaths) {
        for (String path : filePaths) {
            try {
                Files.deleteIfExists(Paths.get(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
