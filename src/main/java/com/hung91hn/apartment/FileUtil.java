package com.hung91hn.apartment;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUtil {
    private final Path root = Paths.get("uploads");

    public void save(String path, MultipartFile file) throws IOException {
        Files.copy(file.getInputStream(), this.root.resolve(path + '/' + file.getOriginalFilename()));
    }
}
