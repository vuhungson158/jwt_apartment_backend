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
    @Query("SELECT p FROM Place p WHERE "
            + "p.latitude > :#{#f.latMin} AND p.latitude < :#{#f.latMax}"
            + " AND p.longitude > :#{#f.lngMin} AND p.longitude < :#{#f.lngMax}"
            + " AND (:#{#f.privateWc} = FALSE OR p.privateWc = 1)"
            + " AND (:#{#f.airConditioner} = FALSE OR p.airConditioner = 1)"
            + " AND (:#{#f.hotWater} = FALSE OR p.hotWater = 1)"
            + " AND (:#{#f.fridge} = FALSE OR p.fridge = 1)"
            + " AND (:#{#f.washer} = FALSE OR p.washer = 1)"
            + " AND (:#{#f.nonCurfew} = FALSE OR p.curfew IS NULL)"
            + " AND p.acreageMin >= :#{#f.acreageMin} AND p.priceMax <= :#{#f.priceMax}")
    List<Place> search(@Param("f") PlaceFilter filter);
}