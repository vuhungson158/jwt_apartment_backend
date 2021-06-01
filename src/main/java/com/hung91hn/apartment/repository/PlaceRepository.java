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
    @Query(nativeQuery = true, value = "SELECT p.*"
            + ", COUNT(IF(v.positive <> 0,1,null)) AS voteCountPositive"
            + ", COUNT(IF(v.positive = 0,1,null)) AS voteCountNegative"
            + " FROM places AS p"
            + " INNER JOIN votes AS v ON p.id = v.place_id"
            + " GROUP BY v.place_id HAVING"
            + " p.latitude > :#{#f.latMin} AND p.latitude < :#{#f.latMax}"
            + " AND p.longitude > :#{#f.lngMin} AND p.longitude < :#{#f.lngMax}"
            + " AND (:#{#f.privateWc} = FALSE OR p.private_wc = 1)"
            + " AND (:#{#f.airConditioner} = FALSE OR p.air_conditioner = 1)"
            + " AND (:#{#f.hotWater} = FALSE OR p.hot_water = 1)"
            + " AND (:#{#f.fridge} = FALSE OR p.fridge = 1)"
            + " AND (:#{#f.washer} = FALSE OR p.washer = 1)"
            + " AND (:#{#f.nonCurfew} = FALSE OR p.curfew IS NULL)"
            + " AND p.acreage_min >= :#{#f.acreageMin}"
            + " AND p.price_max <= :#{#f.priceMax}"
            + " ORDER BY voteCountPositive-voteCountNegative DESC LIMIT 10")
    List<Place> find(@Param("f") PlaceFilter filter);
}