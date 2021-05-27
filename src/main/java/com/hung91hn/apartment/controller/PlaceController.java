package com.hung91hn.apartment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hung91hn.apartment.helper.FileUtil;
import com.hung91hn.apartment.helper.Log;
import com.hung91hn.apartment.model.*;
import com.hung91hn.apartment.repository.PlaceRepository;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

@RestController
@RequestMapping("/place/")
public class PlaceController {
    private final String path = "places/%d.zip";
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private PlaceRepository repository;
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private Log log;

    @RolesAllowed(User.USER)
    @PostMapping("create")
    public Response create(String object, MultipartFile file) throws IOException {
        log.i("POST:/place/create\n" + object);

        final Place place = mapper.readValue(object, Place.class);

        final String err = validate(place);
        if (err != null) return new Response(err);

        final Place _place = repository.save(place);

        if (file != null) fileUtil.save(String.format(path, _place.id), file);

        return new Response(_place);
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
        if (places.isEmpty()) return new Response("Không có phòng nào ở khu vực này thoả mãn yêu cầu của bạn");

        // TODO: viết thêm phần này vào PlaceRepository.search@Query {
        final List<PlaceDto> placeDtos = new ArrayList<>();
        places.forEach(place -> placeDtos.add(new PlaceDto(place)));
        placeDtos.sort((o1, o2) -> (int) ((o1.vote.positive - o1.vote.negative) - (o2.vote.positive - o2.vote.negative)));
        final List<PlaceDto> result = placeDtos.size() < 10 ? placeDtos : placeDtos.subList(0, 10);
        // }

        return new Response(result);
    }

    @PostMapping("loadPictures")
    public void getFile(@RequestBody long id, HttpServletResponse response) {
        try {
            IOUtils.copy(new FileInputStream(fileUtil.root + String.format(path, id)),
                    response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            response.setStatus(HttpStatus.SC_NOT_FOUND);
        }
        log.i("/place/loadPictures: " + response.getStatus());
    }
}
