package com.auction.app.services;

import com.auction.app.model.Bid;

import java.util.List;

public interface BidService {

    Bid createBid(Bid bid);

    void deleteBid(Integer id);

    Bid updateBid(Bid bid);

    Bid getBidById(Integer id);

    List<Bid> getBids();

    Integer updateAllBids();

    Integer updateExpiredItemBids(Integer itemId);
}
