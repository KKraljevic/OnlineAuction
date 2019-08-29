package com.auction.app.services;

import com.auction.app.exceptions.NotFoundException;
import com.auction.app.model.Bid;
import com.auction.app.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    private BidRepository bidRepository;

    @Override
    public Bid createBid(Bid bid) {
        return bidRepository.save(bid);
    }

    @Override
    public void deleteBid(Integer id) {
        bidRepository.findById(id)
                .map(bid -> {
                    bidRepository.delete(bid);
                    return "Deleted Successfully!";
                }).orElseThrow(() -> new NotFoundException("Bid not found!"));
    }

    @Override
    public Bid updateBid(Bid bid) {
        return bidRepository.findById(bid.getBid_id())
                .map(b -> {
                    b.setBidPrice(bid.getBidPrice());
                    b.setActive(bid.isActive());
                    b.setBidTime(bid.getBidTime());
                    return bidRepository.save(b);
                }).get();
    }

    @Override
    public Bid getBidById(Integer id) {
        return bidRepository.findById(id).get();
    }

    @Override
    public List<Bid> getBids() {
        List<Bid> bids = new ArrayList<>();
        bidRepository.findAll().forEach(bids::add);
        return bids;
    }

    @Override
    public Integer updateAllBids() {
        return bidRepository.updateAllExpiredBids(new Date());
    }

    @Override
    public Integer updateExpiredItemBids(Integer itemId) {
        return bidRepository.updateExpiredBidsForItem(itemId);
    }

}
