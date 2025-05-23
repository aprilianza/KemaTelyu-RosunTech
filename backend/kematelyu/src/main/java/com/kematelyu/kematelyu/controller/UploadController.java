package com.kematelyu.kematelyu.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin
public class UploadController {

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String uploadDir = System.getProperty("user.dir") + "/uploads/events/";

        try {
            // Cek dan buat folder jika belum ada
            File dir = new File(uploadDir);
            if (!dir.exists())
                dir.mkdirs();

            // Simpan file ke folder
            File dest = new File(uploadDir + file.getOriginalFilename());
            file.transferTo(dest);

            return ResponseEntity.ok("File uploaded: " + dest.getPath());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gagal upload: " + e.getMessage());
        }
    }
}
