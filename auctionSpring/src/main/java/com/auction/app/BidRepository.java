package com.auction.app;

import com.auction.app.model.Bid;
import org.springframework.data.repository.CrudRepository;

public interface BidRepository extends CrudRepository<Bid,Integer> {
}
