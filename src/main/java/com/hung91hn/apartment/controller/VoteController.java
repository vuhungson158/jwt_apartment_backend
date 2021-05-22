package com.hung91hn.apartment.controller;

import com.hung91hn.apartment.model.Response;
import com.hung91hn.apartment.model.User;
import com.hung91hn.apartment.model.Vote;
import com.hung91hn.apartment.model.VoteCount;
import com.hung91hn.apartment.repository.VoteRepository;
import com.hung91hn.apartment.security.JwtRequestFilter;
import com.hung91hn.apartment.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RolesAllowed(User.USER)
@RestController
@RequestMapping("/vote/")
public class VoteController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private VoteRepository repository;

    @PostMapping("update")
    public Response create(@RequestHeader(JwtRequestFilter.KeyAut) String jwt, @RequestBody Vote vote) {
        vote.userId = jwtUtil.getUser(jwt).id;
        final Vote _vote = repository.findByPlaceIdAndUserId(vote.placeId, vote.userId);
        if (_vote != null) vote.id = _vote.id;
        repository.save(vote);

        return new Response(new VoteCount(repository.findAllByPlaceId(vote.placeId)));
    }

    @PostMapping("get")
    public Response get(@RequestHeader(JwtRequestFilter.KeyAut) String jwt, @RequestBody long placeId) {
        return new Response(repository.findByPlaceIdAndUserId(placeId, jwtUtil.getUser(jwt).id));
    }
}
