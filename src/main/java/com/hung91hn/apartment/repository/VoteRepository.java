package com.hung91hn.apartment.repository;

import com.hung91hn.apartment.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Vote findByPlaceIdAndUserId(long placeId, long userId);

    List<Vote> findAllByPlaceId(long placeId);
}