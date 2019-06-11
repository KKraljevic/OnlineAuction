package com.auction.app;

import com.auction.app.model.Bid;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BidRepository extends CrudRepository<Bid,Integer> {
}
