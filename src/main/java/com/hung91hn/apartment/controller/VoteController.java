package com.hung91hn.apartment.controller;

import com.hung91hn.apartment.model.Response;
import com.hung91hn.apartment.model.User;
import com.hung91hn.apartment.model.Vote;
import com.hung91hn.apartment.model.VoteCount;
import com.hung91hn.apartment.repository.VoteRepository;
import com.hung91hn.apartment.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RolesAllowed(User.USER)
@RestController
@RequestMapping("/vote/")
public class VoteController {
    @Autowired
    private VoteRepository repository;

    @PostMapping("post")
    public Response create(Authentication authentication, @RequestBody Vote vote) {
        vote.userId = ((UserPrincipal) authentication.getPrincipal()).id;
        final Vote _vote = repository.findByPlaceIdAndUserId(vote.placeId, vote.userId);
        if (_vote != null) vote.id = _vote.id;
        repository.save(vote);

        return new Response(new VoteCount(repository.findAllByPlaceId(vote.placeId)));
    }

    @PostMapping("get")
    public Response get(Authentication authentication, @RequestBody long placeId) {
        return new Response(repository.findByPlaceIdAndUserId(placeId, ((UserPrincipal) authentication.getPrincipal()).id));
    }
}
