package com.hung91hn.apartment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hung91hn.apartment.helper.FileUtil;
import com.hung91hn.apartment.helper.Log;
import com.hung91hn.apartment.model.Place;
import com.hung91hn.apartment.model.PlaceFilter;
import com.hung91hn.apartment.model.Response;
import com.hung91hn.apartment.model.ResponseT;
import com.hung91hn.apartment.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static java.lang.Math.abs;

@RestController
@RequestMapping("/place/")
public class PlaceController {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private PlaceRepository repository;
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private Log log;

    @PostMapping("create")
    public Response create(String object, MultipartFile file) throws IOException {
        log.i("POST:/place/create\n" + object);

        final Place place = mapper.readValue(object, Place.class);

        final String err = validate(place);
        if (err != null) return new Response(err);

        final Place _place = repository.save(place);

        if (file != null) fileUtil.save("places/" + _place.id + ".zip", file);

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
        final List<Place> places = repository.search(filter);

        return places.isEmpty() ? new Response("Không có phòng nào ở khu vực này thoả mãn yêu cầu của bạn")
                : new ResponseT<>(places);
    }
}
