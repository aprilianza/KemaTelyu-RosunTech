package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin
public class UploadController {

    private static final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png");
    private static final List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png");

    @PostMapping
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {

        try {
            // Validasi extension
            String originalName = file.getOriginalFilename();
            String extension = "";
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf('.')).toLowerCase();
            }

            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                throw new BaseException(org.springframework.http.HttpStatus.BAD_REQUEST,
                        "Ekstensi file tidak diperbolehkan: " + extension);
            }

            // Validasi MIME type
            String mimeType = file.getContentType();
            if (mimeType == null || !ALLOWED_MIME_TYPES.contains(mimeType)) {
                throw new BaseException(org.springframework.http.HttpStatus.BAD_REQUEST,
                        "Tipe file tidak diperbolehkan: " + mimeType);
            }

            // Simpan ke folder eksternal: /static/events
            Path uploadDir = Paths.get(System.getProperty("user.dir"), "static", "events");
            if (Files.notExists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String randomName = UUID.randomUUID().toString().replace("-", "") + extension;
            Path destination = uploadDir.resolve(randomName);
            file.transferTo(destination);

            String relativePath = "events/" + randomName;
            return ResponseEntity.ok(Map.of(
                    "filePath", relativePath,
                    "message", "File uploaded successfully"
            ));

        } catch (IOException e) {
            throw new BaseException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                    "Gagal upload: " + e.getMessage());
        }
    }
}
