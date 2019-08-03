package com.auction.app.controller;

import com.auction.app.model.*;
import com.auction.app.repository.*;
import com.auction.app.conf.NotFoundException;
import com.auction.app.services.FileService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "https://still-castle-19196.herokuapp.com")
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionItemsRepository itemsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private ImageRepository imageRepository;

    private final FileService fileService;

    @Autowired
    public UserController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/login")
    public User findUser(@RequestBody User user) {
        Optional<User> optionalUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @GetMapping("/users/{id}")
    public User findLogUser(@PathVariable Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new NotFoundException("User not found with id " + id);
        }
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) != null)
            throw new NotFoundException("Email is taken");
        else {
            userRepository.save(user);
            return user;
        }
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        if (users.size() > 0)
            return users;
        else
            throw new NotFoundException("No users found");
    }

    @GetMapping("/users/{userId}/items")
    public Page<AuctionItem> getUsersItems(@PathVariable Integer userId,
                                           @PageableDefault(sort = "id", size = 9, page = 0) Pageable pageable) {

        Page<AuctionItem> items;
        items = itemsRepository.findActiveSellerItems(userId, new Date(), pageable);
        return items;
    }

    @GetMapping("/users/{userId}/items/expired")
    public Page<AuctionItem> getExpiredUsersItems(@PathVariable Integer userId,
                                           @PageableDefault(sort = "id", size = 9, page = 0) Pageable pageable) {

        Page<AuctionItem> items;
        items = itemsRepository.findExpiredSellerItems(userId, new Date(), pageable);
        return items;
    }

    @GetMapping("/users/{userId}/allitems")
    public ResponseEntity<List<AuctionItem>> getUsersItems(@PathVariable Integer userId) {

        List<AuctionItem> items = new ArrayList<>();
        items = itemsRepository.findAllActiveSellerItems(userId, new Date());
        if(items.size()>0){
            return new ResponseEntity<List<AuctionItem>>(items, HttpStatus.OK);
        }
        else{
            throw new NotFoundException("Items not found!");
        }
    }

    @GetMapping("/users/{userId}/hasItems")
    public boolean hasItems(@PathVariable Integer userId) {
        return itemsRepository.existsBySellerId(userId);
    }

    @PostMapping("/users/{userId}/items/{catId}")
    public AuctionItem addAuctionItem(@PathVariable Integer userId, @PathVariable Integer catId,
                                      @Valid @RequestBody AuctionItem item) {
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
        return itemsRepository.save(item);
    }

    @GetMapping("/users/{userId}/bids")
    public Page<Bid> getUserBids(@PathVariable Integer userId,
                                 @PageableDefault(sort = "bidTime",direction = Sort.Direction.DESC,size = 9, page = 0) Pageable pageable) {
        Page<Bid> bids;
        bids = bidRepository.findByBidderId(userId, pageable);
        return bids;
    }

    @GetMapping("/users/{userId}/hasBids")
    public boolean hasBids(@PathVariable Integer userId) {
        return bidRepository.existsByBidderId(userId);
    }

    @PostMapping("/users/{userId}/bids/item/{itemId}")
    public Bid addBid(@PathVariable Integer userId, @PathVariable Integer itemId, @Valid @RequestBody Bid bid) {
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

    @GetMapping("/users/{userId}/allwishlist")
    public ResponseEntity<List<AuctionItem>> getAllWishlist(@PathVariable Integer userId) {
        return new ResponseEntity<List<AuctionItem>>(userRepository.findById(userId).get().getWishlist(), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/wishlist")
    public Page<AuctionItem> getWishlist(@PathVariable Integer userId, @PageableDefault(sort = "id", size = 9, page = 0) Pageable pageable) {
        Page<AuctionItem> wishlistItems;
        wishlistItems = userRepository.findWishlistItems(userId, pageable);
        if (wishlistItems.getTotalElements() > 0)
            return wishlistItems;
        else
            throw new NotFoundException("Items not found!");
    }

    @GetMapping("/users/{userId}/hasWishlist")
    public boolean hasWishlist(@PathVariable Integer userId) {
        return userRepository.hasWishlist(userId);
    }

    @PostMapping("/users/{userId}/wishlist/{itemId}")
    @Transactional
    public ResponseEntity<List<AuctionItem>> addToWishlist(@PathVariable Integer userId, @PathVariable Integer itemId) {
        userRepository.addToWishlist(userId, itemId);
        return getAllWishlist(userId);
    }

    @PostMapping("/users/{userId}/items")
    public AuctionItem addItem(@PathVariable Integer userId, @RequestBody AuctionItem item) {
        AuctionItem newItem = new AuctionItem();
        userRepository.findById(userId).map(user -> {
            item.setSeller(user);
            return user;
        }).orElseThrow(() -> new NotFoundException("Seller not found!"));
        categoryRepository.findById(item.getCategory().getId()).map(cat -> {
            item.setCategory(cat);
            return cat;
        }).orElseThrow(() -> new NotFoundException("Category not found!"));

        Shipping newShipping = new Shipping();
        newShipping = item.getShipping();
        item.setShipping(null);

        List<Image> newImgs = new ArrayList<>();
        newImgs = item.getImages();
        item.setImages(null);

        newItem = itemsRepository.save(item);

        for (Image img : newImgs) {
            img.setItem(newItem);
            imageRepository.save(img);
        }
        newShipping.setShippmentItem(newItem);
        shippingRepository.save(newShipping);

        return newItem;
    }

    @PostMapping("users/{id}/photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        String imgURL;
        try {
            imgURL = fileService.storeFileCloudinary(file);
            JSONObject jo = new JSONObject();
            jo.put("url", imgURL);
            userRepository.findById(id)
                    .map(user -> {
                        user.setPhoto(imgURL);
                        return userRepository.save(user);
                    }).orElseThrow(() -> new NotFoundException("User not found with id " + id));
            return new ResponseEntity<String>(imgURL, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("Image could not be uploaded!");
        }
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User userUpdated) {

        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(userUpdated.getFirstName());
                    user.setLastName(userUpdated.getLastName());
                    user.setGender((userUpdated.getGender()));
                    user.setPhone(userUpdated.getPhone());
                    user.setBirthDate(userUpdated.getBirthDate());
                    user.setPhoto(userUpdated.getPhoto());
                    return userRepository.save(user);
                }).orElseThrow(() -> new NotFoundException("User not found with id " + id));
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Integer id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return "Delete Successfully!";
                }).orElseThrow(() -> new NotFoundException("User not found with id " + id));
    }

}
