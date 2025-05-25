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

    @PostMapping
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {

        try {
            // Simpan ke folder external: /static/events
            Path uploadDir = Paths.get(System.getProperty("user.dir"), "static", "events");

            if (Files.notExists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalName = file.getOriginalFilename();
            String extension = "";
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf('.'));
            }

            String randomName = UUID.randomUUID().toString().replace("-", "") + extension;
            Path destination = uploadDir.resolve(randomName);
            file.transferTo(destination);

            // relative path yg bisa dipakai langsung di FE
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
