package com.auction.app;

import com.auction.app.model.AuctionItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AuctionItemsRepository extends CrudRepository<AuctionItem,Integer> {
    public List<AuctionItem> findBySellerId(Integer id);
}
