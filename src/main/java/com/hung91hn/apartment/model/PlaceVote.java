package com.hung91hn.apartment.model;

public class PlaceVote {
    public Place place;
    public VoteCount vote;

    public PlaceVote(Place place) {
        this.place = place;
        this.vote = new VoteCount(place.votes);
    }
}
