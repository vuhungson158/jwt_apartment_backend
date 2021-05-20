package com.hung91hn.apartment.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
