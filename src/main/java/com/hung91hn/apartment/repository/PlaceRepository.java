package com.hung91hn.apartment.repository;

import com.hung91hn.apartment.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

        /* todo SQL
        longitude > west; < east
        latitude < north; > south
        if  filter
        get all vote with placeId
        ORDER BY voteUP/votes des
        select limit = 10
         */
    //todo bug tại kinh độ gốc

//    @Query("SELECT p FROM Place p WHERE p.latitude > :#{#f.south} AND p.latitude < :#{#f.north} AND p.longitude > :#{#f.west} AND p.longitude < :#{#f.east}")
//    List<Place> search(@Param("filter") PlaceFilter f);
}