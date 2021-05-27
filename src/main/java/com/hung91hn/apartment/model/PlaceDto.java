package com.hung91hn.apartment.model;

public class PlaceDto {
    public Place place;
    public VoteCount vote;

    public PlaceDto(Place place) {
        this.place = place;
        this.vote = new VoteCount(place.votes);
    }
}
