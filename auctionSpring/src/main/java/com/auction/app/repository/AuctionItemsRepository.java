package com.auction.app.repository;

import com.auction.app.model.AuctionItem;
import com.auction.app.model.Bid;
import com.auction.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AuctionItemsRepository extends CrudRepository<AuctionItem, Integer> {

    List<AuctionItem> findBySellerId(Integer id);

    Page<AuctionItem> findAll(Pageable pageable);

    Page<AuctionItem> findBySellerId(Integer id, Pageable pageable);

    Page<AuctionItem> findBySellerIdAndPaid(Integer id, Pageable pageable);

    Page<AuctionItem> findByCategoryId(Integer id, Pageable pageable);

    Page<AuctionItem> findByNameContaining(String name, Pageable pageable);

    boolean existsBySellerId(Integer id);

    @Query("select b.bidder from Bid b join b.bidder b1 join b.auctionItem ai where ai.id=?1 and ai.currentPrice=b.bidPrice")
    User findHighestBidder(Integer itemId);

    @Query("select ai from AuctionItem ai join ai.seller s where s.id=?1 and ai.endDate < ?2 and ai.paid=false and ai.currentPrice=ai.startPrice")
    Page<AuctionItem> findExpiredSellerItems(Integer id, Date nowDate, Pageable pageable);

    @Query("select ai from AuctionItem ai join ai.seller s where s.id=?1 and ai.endDate < ?2 and ai.paid=false and ai.currentPrice>ai.startPrice")
    Page<AuctionItem> findPendingSellerItems(Integer id, Date nowDate, Pageable pageable);

    @Query("select ai from AuctionItem ai join ai.seller s where s.id=?1 and ai.endDate < ?2 and ai.paid=true")
    Page<AuctionItem> findSoldSellerItems(Integer id, Date nowDate, Pageable pageable);

    @Query("select count(ai) from AuctionItem ai join ai.seller s where s.id=?1 and ai.endDate < ?2 and ai.paid=false")
    Integer countExpiredItems(Integer id, Date nowDate);

    @Query("select ai from AuctionItem ai join ai.seller s where s.id=?1 and ai.endDate > ?2")
    List<AuctionItem> findAllActiveSellerItems(Integer id, Date nowDate);

    @Query("select ai from AuctionItem ai join ai.seller s where s.id=?1 and ai.endDate > ?2")
    Page<AuctionItem> findActiveSellerItems(Integer id, Date nowDate, Pageable pageable);

    @Query("select ai from AuctionItem ai join ai.category c where c.id=?1" +
            " and ai.endDate > ?2")
    Page<AuctionItem> findAllCategoryItems(Integer catId, Date nowDate, Pageable pageable);

    @Query("select ai from AuctionItem ai join ai.seller s where ai.endDate > ?1")
    Page<AuctionItem> findAllShopItems(Date nowDate, Pageable pageable);

    @Query("select ai from AuctionItem ai join ai.seller s where lower(ai.name) like %?1% and ai.endDate > ?2")
    Page<AuctionItem> findAllSearchItems(String search, Date nowDate, Pageable pageable);

    @Query("select ai from AuctionItem ai join ai.seller s where s.id<>?1 and ai.endDate > ?2")
    Page<AuctionItem> findFirst4ShopItems(Integer userId, Date nowDate, Pageable pageable);

    @Query("select ai from AuctionItem ai where ai.endDate > ?1")
    Page<AuctionItem> findFirst4Items(Date nowDate, Pageable pageable);

}
