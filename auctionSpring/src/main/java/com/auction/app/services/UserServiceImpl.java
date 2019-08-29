package com.auction.app.services;

import com.auction.app.exceptions.NotFoundException;
import com.auction.app.model.*;
import com.auction.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionItemsRepository itemsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public User loginUser(User user) {
        Optional<User> optionalUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public User getUser(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new NotFoundException("User not found with id " + id);
        }
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        if (users.size() > 0)
            return users;
        else
            throw new NotFoundException("No users found");
    }

    @Override
    public User createUser(@NotNull User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new NotFoundException("Email is taken");
        } else {
            userRepository.save(user);
            return user;
        }
    }

    @Override
    public User updateUser(@NotNull User userUpdated) {
        return userRepository.findById(userUpdated.getId())
                .map(user -> {
                    user.setFirstName(userUpdated.getFirstName());
                    user.setLastName(userUpdated.getLastName());
                    user.setGender((userUpdated.getGender()));
                    user.setPhone(userUpdated.getPhone());
                    user.setBirthDate(userUpdated.getBirthDate());
                    user.setPhoto(userUpdated.getPhoto());
                    return userRepository.save(user);
                }).orElseThrow(() -> new NotFoundException("User not found with id " + userUpdated.getId()));
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                }).orElseThrow(() -> new NotFoundException("User not found with id " + id));
    }

    @Override
    public List<AuctionItem> getAllActiveUserItems(Integer id) {
        return itemsRepository.findAllActiveSellerItems(id, new Date());
    }

    @Override
    public List<AuctionItem> getWishlist(Integer id) {
        return userRepository.findById(id).get().getWishlist();
    }

    @Override
    public Page<Bid> getActiveUserBidsPage(Integer id, Pageable pageable) {
        return bidRepository.findByBidderIdAndActive(id, true, pageable);
    }

    @Override
    public Page<AuctionItem> getActiveUserItemsPage(Integer id, Pageable pageable) {
        return itemsRepository.findActiveSellerItems(id, new Date(), pageable);
    }

    @Override
    public Page<Bid> getExpiredUserBidsPage(Integer id, Pageable pageable) {
        return bidRepository.findByBidderIdAndActive(id, false, pageable);
    }

    @Override
    public Page<Bid> getLostUserBidsPage(Integer id, Pageable pageable) {
        return bidRepository.getLostUserBids(id, new Date(), pageable);
    }

    @Override
    public Page<Bid> getWonUserBidsPage(Integer id, Pageable pageable) {
        return bidRepository.getWonUserBids(id, new Date(), pageable);
    }

    @Override
    public List<Rating> getUserRatings(Integer id) {
        return ratingRepository.findAllByRatedSellerId(id);
    }

    @Override
    public Float getUserRating(Integer id) {
        return ratingRepository.calculateUserRating(id);
    }

    @Override
    public Rating getUserRatingByBidder(Integer userId, Integer bidderId) {
        return ratingRepository.findByRatedSellerIdAndBidder(userId, bidderId);
    }

    @Override
    public Page<AuctionItem> getExpiredUserItemsPage(Integer id, Pageable pageable) {
        return itemsRepository.findExpiredSellerItems(id, new Date(), pageable);
    }

    @Override
    public Page<AuctionItem> getPendingUserItemsPage(Integer id, Pageable pageable) {
        return itemsRepository.findPendingSellerItems(id, new Date(), pageable);
    }

    @Override
    public Page<AuctionItem> getSoldUserItemsPage(Integer id, Pageable pageable) {
        return itemsRepository.findSoldSellerItems(id, new Date(), pageable);
    }

    @Override
    public Page<AuctionItem> getWishlistPage(Integer id, Pageable pageable) {
        return userRepository.findWishlistItems(id, pageable);
    }

    @Override
    public boolean hasItems(Integer id) {
        return itemsRepository.existsBySellerId(id);
    }

    @Override
    public boolean hasBids(Integer id) {
        return bidRepository.existsByBidderId(id);
    }

    @Override
    public boolean hasWishlist(Integer id) {
        return userRepository.hasWishlist(id);
    }

    @Override
    public Integer countExpiredItems(Integer userId) {
        return itemsRepository.countExpiredItems(userId, new Date());
    }

    @Override
    public Integer countWonBids(Integer userId) {
        return bidRepository.countWonUserBids(userId, new Date());
    }

    @Override
    public AuctionItem addItem(Integer userId, Integer catId, AuctionItem item) {
        userRepository.findById(userId)
                .map(user -> {
                    item.setSeller(user);
                    return true;
                }).orElseThrow(() -> new NotFoundException("User not found!"));
        categoryRepository.findById(catId).map(
                cat -> {
                    item.setCategory(cat);
                    return true;
                }).orElseThrow(() -> new NotFoundException("Category not found!"));

        List<Image> images = new ArrayList<>();
        item.getImages().forEach(images::add);
        item.setImages(null);

        item.setLocation(shippingRepository.save(item.getLocation()));

        AuctionItem newItem = itemsRepository.save(item);
        images.forEach(image -> image.setItem(newItem));
        System.out.println(images.toString());
        imageRepository.saveAll(images);
        return newItem;
    }

    @Override
    public Bid addBid(Integer userId, Integer itemId, Bid bid) {
        userRepository.findById(userId).map(user -> {
            bid.setBidder(user);
            return user;
        }).orElseThrow(() -> new NotFoundException("Bidder not found!"));
        itemsRepository.findById(itemId).map(item -> {
            if (item.getCurrentPrice() < bid.getBidPrice()) {
                item.setCurrentPrice(bid.getBidPrice());
                itemsRepository.save(item);
            }
            bid.setAuctionItem(item);
            return item;
        }).orElseThrow(() -> new NotFoundException("Item not found!"));
        return bidRepository.save(bid);
    }

    @Override
    public User addRating(Integer sellerId, Rating rating) {
        return userRepository.findById(sellerId)
                .map(user -> {
                    rating.setRatedSeller(user);
                    ratingRepository.save(rating);
                    return user;
                }).orElseThrow(() -> new NotFoundException("User not found!"));
    }


}
