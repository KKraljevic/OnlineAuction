package com.auction.app.services;

import com.auction.app.exceptions.NotFoundException;
import com.auction.app.model.AuctionItem;
import com.auction.app.model.Bid;
import com.auction.app.model.Location;
import com.auction.app.model.User;
import com.auction.app.repository.AuctionItemsRepository;
import com.auction.app.repository.BidRepository;
import com.auction.app.repository.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuctionItemServiceImpl implements AuctionItemService {

    @Autowired
    AuctionItemsRepository itemRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ShippingRepository shippingRepository;

    @Override
    public AuctionItem createItem(AuctionItem item) {
        return null;
    }

    @Override
    public AuctionItem updateItem(AuctionItem item) {
        return itemRepository.save(item);
    }

    @Override
    public void deleteItem(Integer id) {
        itemRepository.findById(id)
                .map(item -> {
                    itemRepository.delete(item);
                    return true;
                }).orElseThrow(() -> new NotFoundException("Auction item not found!"));
    }

    @Override
    public AuctionItem getItem(Integer itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException("Item not found!");
        } else {
            return itemRepository.findById(itemId).get();
        }
    }

    @Override
    public User getHighestBidder(Integer itemId) {
        return itemRepository.findHighestBidder(itemId);
    }

    @Override
    public Page<AuctionItem> getItemsPage(Pageable pageable, Integer userId) {

        if (userId != 0) {
            return itemRepository.findFirst4ShopItems(userId, new Date(),
                    PageRequest.of(0, 4, new Sort(Sort.Direction.ASC, "endDate")));
        } else {
            return itemRepository.findAllShopItems(new Date(), pageable);
        }
    }

    @Override
    public Page<AuctionItem> findSearchItemsPage(String search, Pageable pageable) {
        return itemRepository.findAllSearchItems(search, new Date(), pageable);
    }

    @Override
    public List<AuctionItem> getFeaturedItems(Integer userId) {
        if (userId != 0) {
            return itemRepository.findFirst4ShopItems(userId, new Date(), PageRequest.of(0, 4, Sort.by("id").descending())).getContent();
        } else {
            return itemRepository.findFirst4Items(new Date(), PageRequest.of(0, 4, Sort.by("id").descending())).getContent();
        }
    }

    @Override
    public Page<Bid> getItemBidsPage(Integer itemId, Pageable pageable) {
        return bidRepository.findByAuctionItemId(itemId, pageable);
    }

    @Override
    public Integer deactivateItemBids(Integer itemId) {
        return bidRepository.updateExpiredBidsForItem(itemId);
    }

    @Override
    public Location addShippment(Integer itemId, Location location) {
        return itemRepository.findById(itemId).map( item ->
        {
            item.setShipmentLocation(location);
            item.setPaid(true);
            itemRepository.save(item);
            return shippingRepository.save(location);
        }).orElseThrow(() -> new NotFoundException("Item not found!"));
    }

}
