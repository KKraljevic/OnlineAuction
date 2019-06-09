package com.auction.app;

import com.auction.app.model.AuctionItem;
import org.springframework.data.repository.CrudRepository;

public interface AuctionItemsRepository extends CrudRepository<AuctionItem,Integer> {
}
