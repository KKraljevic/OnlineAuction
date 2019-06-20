package com.auction.app;

import com.auction.app.model.AuctionItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AuctionItemsRepository extends CrudRepository<AuctionItem,Integer> {
    public Page<AuctionItem> findAll(Pageable pageable);
    public List<AuctionItem> findBySellerId(Integer id);
}
