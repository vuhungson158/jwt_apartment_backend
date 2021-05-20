package com.hung91hn.apartment.repository;

import com.hung91hn.apartment.model.Place;
import com.hung91hn.apartment.model.PlaceFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

        /* todo SQL
        if  filter
        get all vote with placeId
        ORDER BY voteUP/votes des
        select limit = 10
         */

    @Query("SELECT p FROM Place p WHERE " +
            "p.latitude > :#{#f.latMin} AND p.latitude < :#{#f.latMax} AND p.longitude > :#{#f.lngMin} AND p.longitude < :#{#f.lngMax}"
            /*+ " and (:#{#f.privateWC} = 0 or p.privateWC = :#{#f.privateWC})"*/)
    List<Place> search(@Param("f") PlaceFilter filter);
}