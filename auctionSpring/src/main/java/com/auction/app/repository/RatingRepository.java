package com.auction.app.repository;

import com.auction.app.model.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RatingRepository extends CrudRepository<Rating, Integer> {
    boolean existsByBidderAndRatedSellerId(Integer bidderId, Integer sellerId);

    List<Rating> findAllByRatedSellerId(Integer id);

    Rating findByRatedSellerIdAndBidder(Integer sellerId, Integer bidderId);

    @Query("SELECT AVG(r.value) FROM Rating r JOIN r.ratedSeller s WHERE s.id = ?1")
    Float calculateUserRating(Integer userId);
}
