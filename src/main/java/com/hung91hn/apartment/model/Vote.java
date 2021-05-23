package com.hung91hn.apartment.model;

import javax.persistence.*;

@Entity
@Table(name = "votes")
public class Vote {
    @Id
    @GeneratedValue
    public long id;
    public boolean positive;
    public long userId, placeId;
}
