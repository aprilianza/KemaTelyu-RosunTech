package com.kematelyu.kematelyu.controller;

import com.kematelyu.kematelyu.exception.ForbiddenException;
import com.kematelyu.kematelyu.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin
public class UploadController {

    @PostMapping
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {

        String uploadDir = System.getProperty("user.dir") + "/uploads/events/";

        try {
            File dir = new File(uploadDir);
            if (!dir.exists())
                dir.mkdirs();

            File dest = new File(uploadDir + file.getOriginalFilename());
            file.transferTo(dest);

            return ResponseEntity.ok(Map.of("message", "File uploaded: " + dest.getPath()));
        } catch (IOException e) {
            throw new BaseException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                    "Gagal upload: " + e.getMessage());
        }
    }
}
