package com.hung91hn.apartment.helper;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUtil {
    public final String root = "file/upload/";

    public void save(String path, MultipartFile file) throws IOException {
        final File f = new File(root + path);
        if ((f.getParentFile().exists() || f.getParentFile().mkdirs()) && (f.exists() || f.createNewFile()))
            file.transferTo(f.toPath());
    }
}
