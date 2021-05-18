package com.hung91hn.apartment.controller;

import com.hung91hn.apartment.FileUtil;
import com.hung91hn.apartment.model.Place;
import com.hung91hn.apartment.model.PlaceFilter;
import com.hung91hn.apartment.model.Response;
import com.hung91hn.apartment.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static java.lang.Math.abs;

@RestController
@RequestMapping("/place/")
public class PlaceController {
    @Autowired
    private PlaceRepository repository;
    @Autowired
    private FileUtil fileUtil;

    @PostMapping("create")
    public Response create(Place object, MultipartFile file) throws IOException {
        final String err = validate(object);
        if (err != null) return new Response(err);

        final Place place = repository.save(object);

        if (file != null) fileUtil.save("places/" + place.id, file);

        return new Response();
    }

    private String validate(Place place) {
        if (place == null) return "Không có địa điểm";
        if (abs(place.latitude) > 90 || abs(place.longitude) > 180) return "Lỗi toạ độ";
        if (!StringUtils.hasLength(place.name)) return "Thiếu tên";
        // TODO: 5/16/21

        return null;
    }


    @PostMapping("gets")
    public Response gets(@RequestBody PlaceFilter filter) {
        //todo bug tại kinh độ gốc



        return new Response();
    }
}