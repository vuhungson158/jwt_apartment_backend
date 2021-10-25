package com.hung91hn.apartment.model;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "placesView")
@Immutable
public class PlacesView {

    @Id
    @GeneratedValue()
    public long id;
    public double latitude, longitude;
    public String name, address, hostName, hostPhone, moreInfo;
    public boolean privateWc, airConditioner, hotWater, fridge, washer;
    public int acreageMin, acreageMax, priceMin, priceMax, roomCount, parkCapacity;
    public Integer curfew;
    public Long hostId, inaugurate;

////    @Transient
    public long voteCountPositive;
////    @Transient
    public long voteCountNegative;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "placeId")
    public List<Comment> comments;
}
