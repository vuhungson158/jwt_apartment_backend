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
    public int averageMin, averageMax, priceMin, priceMax, roomCount, parkCapacity, curfew;
    public long ownerId, inaugurate;
}
