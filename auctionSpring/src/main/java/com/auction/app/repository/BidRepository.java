package com.auction.app.repository;

import com.auction.app.model.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface BidRepository extends CrudRepository<Bid, Integer> {

    public Page<Bid> findByAuctionItemId(Integer id, Pageable pageable);

    public Page<Bid> findByBidderId(Integer id, Pageable pageable);

    public Page<Bid> findByBidderIdAndActive(Integer id, boolean active, Pageable pageable);

    public boolean existsByBidderId(Integer id);

    @Transactional
    @Modifying
    @Query("update Bid bid set bid.active=false where bid.auctionItem.id=?1")
    int updateExpiredBidsForItem(Integer itemId);

    @Transactional
    @Modifying
    @Query(value = "update public.bids set active=false from public.auction_items as ai where ai.item_id=public.bids.item_id and ai.end_date<?1", nativeQuery = true)
    int updateAllExpiredBids(Date nowDate);

    @Query("select count(b) from Bid b join b.bidder b1 join b.auctionItem ai where b1.id=?1 and ai.endDate < ?2 and ai.paid=false and ai.currentPrice=b.bidPrice")
    Integer countWonUserBids(Integer userId, Date nowDate);

    @Query("select b from Bid b join b.bidder b1 join b.auctionItem ai where b1.id=?1 and ai.endDate < ?2 and ai.currentPrice!=b.bidPrice group by b.bidTime,b.id")
    Page<Bid> getLostUserBids(Integer userId, Date nowDate, Pageable pageable);

    @Query("select b from Bid b join b.bidder b1 join b.auctionItem ai where b1.id=?1 and ai.endDate < ?2 and ai.currentPrice=b.bidPrice order by ai.paid")
    Page<Bid> getWonUserBids(Integer userId, Date nowDate, Pageable pageable);


}
