package com.auction.app.services;

import com.auction.app.model.AuctionItem;
import com.auction.app.model.Bid;
import com.auction.app.model.Rating;
import com.auction.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface UserService {
    User loginUser(User user);

    User getUser(Integer id);

    List<User> getUsers();

    User createUser(User user);

    User updateUser(User updatedUser);

    void deleteUser(Integer id);

    List<AuctionItem> getWishlist(Integer id);

    Page<AuctionItem> getWishlistPage(Integer id, Pageable pageable);

    List<AuctionItem> getAllActiveUserItems(Integer id);

    Page<AuctionItem> getActiveUserItemsPage(Integer id, Pageable pageable);

    Page<AuctionItem> getExpiredUserItemsPage(Integer id, Pageable pageable);

    Page<AuctionItem> getPendingUserItemsPage(Integer id, Pageable pageable);

    Page<AuctionItem> getSoldUserItemsPage(Integer id, Pageable pageable);

    Page<Bid> getActiveUserBidsPage(Integer id, Pageable pageable);

    Page<Bid> getExpiredUserBidsPage(Integer id, Pageable pageable);

    Page<Bid> getLostUserBidsPage(Integer id, Pageable pageable);

    Page<Bid> getWonUserBidsPage(Integer id, Pageable pageable);

    List<Rating> getUserRatings(Integer id);

    Float getUserRating(Integer id);

    Rating getUserRatingByBidder(Integer userId, Integer bidderId);

    boolean hasItems(Integer id);

    boolean hasBids(Integer id);

    boolean hasWishlist(Integer id);

    Integer countExpiredItems(Integer userId);

    Integer countWonBids(Integer userId);

    AuctionItem addItem(Integer userId, Integer catId, AuctionItem item);

    Bid addBid(Integer userId, Integer itemId, Bid bid);

    User addRating (Integer sellerId, Rating rating);
}
