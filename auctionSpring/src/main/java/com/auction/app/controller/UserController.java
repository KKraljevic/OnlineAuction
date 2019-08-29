package com.auction.app.controller;

import com.auction.app.model.*;
import com.auction.app.repository.*;
import com.auction.app.exceptions.NotFoundException;
import com.auction.app.upload.CloudinaryUploadService;
import com.auction.app.services.UserService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "https://still-castle-19196.herokuapp.com")
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UserController {

    final static int pageSize = 9;

    @Autowired
    private UserRepository userRepository;

    private final CloudinaryUploadService cloudinaryUploadService;

    private final UserService userService;

    @Autowired
    public UserController(CloudinaryUploadService cloudinaryUploadService, UserService userService) {
        this.cloudinaryUploadService = cloudinaryUploadService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public User findUser(@RequestBody User user) {
        return userService.loginUser(user);
    }

    @GetMapping("/users/{id}")
    public User findLogUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{userId}/rating")
    public ResponseEntity<Float> getUserRating(@PathVariable Integer userId){
        return new ResponseEntity<>(userService.getUserRating(userId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/rating/{bidderId}")
    public ResponseEntity<Rating> getUserRatingByBidder(@PathVariable Integer userId, @PathVariable Integer bidderId){
        return new ResponseEntity<>(userService.getUserRatingByBidder(userId, bidderId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/ratings")
    public ResponseEntity<List<Rating>> getUserRatings(@PathVariable Integer userId){
        return new ResponseEntity<>(userService.getUserRatings(userId), HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/rating")
    public User setUserRating(@PathVariable Integer userId,@RequestBody Rating rating){
        return userService.addRating(userId,rating);
    }

    @GetMapping("/users/{userId}/items/active")
    public Page<AuctionItem> getActiveUsersItems(@PathVariable Integer userId,
                                           @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = pageSize, page = 0) Pageable pageable) {
        return userService.getActiveUserItemsPage(userId, pageable);
    }

    @GetMapping("/users/{userId}/items/expired")
    public Page<AuctionItem> getExpiredUsersItems(@PathVariable Integer userId,
                                                  @PageableDefault(sort = "endDate", direction = Sort.Direction.DESC, size = pageSize, page = 0) Pageable pageable) {
        return userService.getExpiredUserItemsPage(userId, pageable);
    }

    @GetMapping("/users/{userId}/items/sold")
    public Page<AuctionItem> getSoldUsersItems(@PathVariable Integer userId,
                                               @PageableDefault(sort = "endDate", direction = Sort.Direction.DESC, size = pageSize, page = 0) Pageable pageable) {
        return userService.getSoldUserItemsPage(userId, pageable);
    }

    @GetMapping("/users/{userId}/items/pending")
    public Page<AuctionItem> getPendingUsersItems(@PathVariable Integer userId,
                                                  @PageableDefault(sort = "endDate", direction = Sort.Direction.DESC, size = pageSize, page = 0) Pageable pageable) {
        return userService.getPendingUserItemsPage(userId, pageable);
    }

    @GetMapping("/users/{userId}/items/expired/count")
    public ResponseEntity<String> countExpiredItems(@PathVariable Integer userId) {
        return new ResponseEntity<>(userService.countExpiredItems(userId).toString(), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/allitems")
    public ResponseEntity<List<AuctionItem>> getUsersItems(@PathVariable Integer userId) {
        return new ResponseEntity<List<AuctionItem>>(userService.getAllActiveUserItems(userId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/hasItems")
    public boolean hasItems(@PathVariable Integer userId) {
        return userService.hasItems(userId);
    }

    @PostMapping("/users/{userId}/items/{catId}")
    public AuctionItem addAuctionItem(@PathVariable Integer userId, @PathVariable Integer catId,
                                      @Valid @RequestBody AuctionItem item) {
        return userService.addItem(userId, catId, item);
    }

    @GetMapping("/users/{userId}/bids/active")
    public Page<Bid> getUserBids(@PathVariable Integer userId,
                                 @PageableDefault(sort = "bidTime", direction = Sort.Direction.DESC, size = pageSize, page = 0) Pageable pageable) {
        return userService.getActiveUserBidsPage(userId, pageable);
    }

    @GetMapping("/users/{userId}/bids/expired")
    public ResponseEntity<Page<Bid>> getExpiredUserBids(@PathVariable Integer userId,
                                                        @PageableDefault(sort = "bidTime", direction = Sort.Direction.DESC, size = pageSize, page = 0) Pageable pageable) {
        return new ResponseEntity<>(userService.getExpiredUserBidsPage(userId,pageable), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/bids/lost")
    public ResponseEntity<Page<Bid>> getLostUserBids(@PathVariable Integer userId,
                                                        @PageableDefault(sort = "bidTime", direction = Sort.Direction.DESC, size = pageSize, page = 0) Pageable pageable) {
        return new ResponseEntity<>(userService.getLostUserBidsPage(userId,pageable), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/bids/won")
    public ResponseEntity<Page<Bid>> getWonUserBids(@PathVariable Integer userId,
                                                        @PageableDefault(sort = "bidTime", direction = Sort.Direction.DESC, size = pageSize, page = 0) Pageable pageable) {
        return new ResponseEntity<>(userService.getWonUserBidsPage(userId,pageable), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/bids/won/count")
    public ResponseEntity<String> countWonBids(@PathVariable Integer userId) {
        return new ResponseEntity<>(userService.countWonBids(userId).toString(), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/hasBids")
    public boolean hasBids(@PathVariable Integer userId) {
        return userService.hasBids(userId);
    }

    @PostMapping("/users/{userId}/bids/item/{itemId}")
    public Bid addBid(@PathVariable Integer userId, @PathVariable Integer itemId, @Valid @RequestBody Bid bid) {
        return userService.addBid(userId, itemId, bid);
    }

    @GetMapping("/users/{userId}/allwishlist")
    public ResponseEntity<List<AuctionItem>> getAllWishlist(@PathVariable Integer userId) {
        return new ResponseEntity<>(userService.getWishlist(userId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/wishlist")
    public Page<AuctionItem> getWishlist(@PathVariable Integer userId, @PageableDefault(sort = "endDate", direction = Sort.Direction.DESC, size = pageSize, page = 0) Pageable pageable) {
        return userService.getWishlistPage(userId, pageable);
    }

    @GetMapping("/users/{userId}/hasWishlist")
    public boolean hasWishlist(@PathVariable Integer userId) {
        return userService.hasWishlist(userId);
    }

    @PostMapping("/users/{userId}/wishlist/{itemId}")
    @Transactional
    public ResponseEntity<List<AuctionItem>> addToWishlist(@PathVariable Integer userId, @PathVariable Integer itemId) {
        userRepository.addToWishlist(userId, itemId);
        return new ResponseEntity<>(userService.getWishlist(userId), HttpStatus.OK);
    }

    @PostMapping("users/{id}/photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        String imgURL;
        try {
            imgURL = cloudinaryUploadService.storeFileCloudinary(file);
            JSONObject jo = new JSONObject();
            jo.put("url", imgURL);
            userRepository.findById(id)
                    .map(user -> {
                        user.setPhoto(imgURL);
                        return userRepository.save(user);
                    }).orElseThrow(() -> new NotFoundException("User not found with id " + id));
            return new ResponseEntity<>(imgURL, HttpStatus.OK);
        } catch (final IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("Image could not be uploaded!");
        }
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User userUpdated) {
        return userService.updateUser(userUpdated);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

}
