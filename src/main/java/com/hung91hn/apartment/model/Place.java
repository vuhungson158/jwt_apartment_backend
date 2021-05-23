package com.hung91hn.apartment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "places")
public class Place {
    @Id
    @GeneratedValue()
    public long id;
    public double latitude, longitude;
    public String name, address, moreInfo;
    public boolean privateWc, airConditioner, hotWater, fridge, washer;
    public int acreageMin, acreageMax, priceMin, priceMax, roomCount, parkCapacity;
    public Integer curfew;
    public Long ownerId, inaugurate;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "placeId")
    public List<Vote> votes;
}
