package com.auction.app.repository;

import com.auction.app.model.AuctionItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AuctionItemsRepository extends CrudRepository<AuctionItem, Integer> {

    public List<AuctionItem> findBySellerId(Integer id);

    public Page<AuctionItem> findAll(Pageable pageable);

    public Page<AuctionItem> findBySellerId(Integer id,Pageable pageable);

    public Page<AuctionItem> findByCategoryId(Integer id, Pageable pageable);

    public Page<AuctionItem> findByNameContaining(String name, Pageable pageable);

    @Query("select ai from AuctionItem ai join ai.seller s where s.id=?1 and ai.endDate < ?2")
    Page<AuctionItem> findExpiredSellerItems(Integer id,Date nowDate,Pageable pageable);

    @Query("select ai from AuctionItem ai join ai.seller s where s.id=?1 and ai.endDate > ?2")
    List<AuctionItem> findAllActiveSellerItems(Integer id,Date nowDate);

    @Query("select ai from AuctionItem ai join ai.seller s where s.id=?1 and ai.endDate > ?2")
    Page<AuctionItem> findActiveSellerItems(Integer id,Date nowDate,Pageable pageable);

    @Query("select ai from AuctionItem ai join ai.category c where c.id=?1" +
            " and ai.endDate > ?2" )
    Page<AuctionItem> findAllCategoryItems(Integer catId,Date nowDate, Pageable pageable);

    @Query("select ai from AuctionItem ai join ai.seller s where ai.endDate > ?1")
    Page<AuctionItem> findAllShopItems(Date nowDate,Pageable pageable);

    @Query("select ai from AuctionItem ai join ai.seller s where lower(ai.name) like %?1% and ai.endDate > ?2")
    Page<AuctionItem> findAllSearchItems(String search,Date nowDate,Pageable pageable);

    @Query("select ai from AuctionItem ai join ai.seller s where s.id<>?1 and ai.endDate > ?2")
    Page<AuctionItem> findFirst4ShopItems(Integer userId,Date nowDate,Pageable pageable);

    @Query("select ai from AuctionItem ai where ai.endDate > ?1")
    Page<AuctionItem> findFirst4Items(Date nowDate,Pageable pageable);

    public boolean existsBySellerId(Integer id);
}
