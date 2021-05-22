package com.hung91hn.apartment.model;

public class PlaceVote {
    public Place place;
    public VoteCount vote;

    public PlaceVote(Place place, VoteCount vote) {
        this.place = place;
        this.vote = vote;
    }
}
