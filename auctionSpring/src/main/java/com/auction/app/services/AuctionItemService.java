package com.auction.app.services;

import com.auction.app.model.AuctionItem;
import com.auction.app.model.Bid;
import com.auction.app.model.Location;
import com.auction.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuctionItemService {

    AuctionItem createItem(AuctionItem item);

    AuctionItem updateItem(AuctionItem item);

    void deleteItem(Integer id);

    AuctionItem getItem(Integer itemId);

    User getHighestBidder(Integer userId);

    Page<AuctionItem> getItemsPage(Pageable pageable, Integer userId);

    Page<AuctionItem> findSearchItemsPage(String search, Pageable pageable);

    List<AuctionItem> getFeaturedItems(Integer userId);

    Page<Bid> getItemBidsPage(Integer itemId, Pageable pageable);

    Integer deactivateItemBids(Integer itemId);

    Location addShippment(Integer itemId, Location location);

}
