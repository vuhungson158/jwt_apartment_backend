package com.hung91hn.apartment.controller;

import com.hung91hn.apartment.helper.FileUtil;
import com.hung91hn.apartment.helper.Log;
import com.hung91hn.apartment.model.Response;
import com.hung91hn.apartment.model.User;
import com.hung91hn.apartment.security.UserPrincipal;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/avatar/")
public class AvatarController {
    private final String path = "avatar/%d/";
    @Autowired
    private Log log;
    @Autowired
    private FileUtil fileUtil;

    @RolesAllowed(User.USER)
    @PostMapping("post")
    public Response post(Authentication authentication, MultipartFile file) throws IOException {
        final long id = ((UserPrincipal) authentication.getPrincipal()).id;
        log.i("POST:/avatar/post: " + id);

        final File folder = new File(fileUtil.root + String.format(path, id));
        if (folder.exists()) for (File f : folder.listFiles()) f.delete();
        fileUtil.save(String.format(path, id) + file.getOriginalFilename(), file);
        return new Response();
    }

    @PostMapping("load")
    public void load(@RequestBody long userId, HttpServletResponse response) {
        final File[] files = new File(fileUtil.root + String.format(path, userId)).listFiles();
        if (files != null && files.length > 0) try {
            IOUtils.copy(new FileInputStream(files[0]), response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            response.setStatus(HttpStatus.SC_NOT_FOUND);
        }
        else response.setStatus(HttpStatus.SC_NOT_FOUND);
        log.i("/avatar/load: " + response.getStatus());
    }
}
