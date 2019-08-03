package com.auction.app.repository;

import com.auction.app.model.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BidRepository extends CrudRepository<Bid, Integer> {
    public Page<Bid> findByAuctionItemId(Integer id, Pageable pageable);
    public Page<Bid> findByBidderId(Integer id,Pageable pageable);
    public boolean existsByBidderId(Integer id);
}
