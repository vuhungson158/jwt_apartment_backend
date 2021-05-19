package com.hung91hn.apartment;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUtil {
    public void save(String path, MultipartFile file) throws IOException {
        final File f = new File("file/upload/" + path);
        if ((f.getParentFile().exists() || f.getParentFile().mkdirs()) && (f.exists() || f.createNewFile()))
            file.transferTo(f.toPath());
    }
}
