package com.auction.app.controller;

import com.auction.app.exceptions.NotFoundException;
import com.auction.app.model.*;
import com.auction.app.repository.CityRepository;
import com.auction.app.repository.CountryRepository;
import com.auction.app.services.AuctionItemService;
import com.auction.app.services.payment.StripeClient;
import com.auction.app.upload.CloudinaryUploadService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "https://still-castle-19196.herokuapp.com")
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ItemController {

    final static int pageSize = 9;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    private final CloudinaryUploadService cloudinaryUploadService;
    private final StripeClient stripeClient;
    private final AuctionItemService auctionItemService;

    @Autowired
    public ItemController(CloudinaryUploadService cloudinaryUploadService, StripeClient stripeClient, AuctionItemService auctionItemService) {
        this.cloudinaryUploadService = cloudinaryUploadService;
        this.stripeClient = stripeClient;
        this.auctionItemService = auctionItemService;
    }

    @PostMapping(value = "/uploadImage")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String imgURL;
        try {
            imgURL = cloudinaryUploadService.storeFileCloudinary(file);
            JSONObject jo = new JSONObject();
            jo.put("url", imgURL);
            return new ResponseEntity<String>(imgURL, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("Image could not be uploaded!");
        }
    }

    @GetMapping("/items")
    public Page<AuctionItem> getAllItems(@PageableDefault(sort = "id", size = pageSize, page = 0) Pageable pageable,
                                         @RequestParam(value = "user", defaultValue = "0", required = false) Integer user) {
        return auctionItemService.getItemsPage(pageable, user);
    }

    @GetMapping("/search")
    public Page<AuctionItem> getSearchItems(@PageableDefault(sort = "id", size = pageSize, page = 0) Pageable pageable,
                                            @RequestParam(value = "search", defaultValue = "") String search) {
        return auctionItemService.findSearchItemsPage(search, pageable);
    }

    @GetMapping("/featuredItems")
    public List<AuctionItem> getFeaturedItems(@RequestParam(value = "user", defaultValue = "0", required = false) Integer user) {
        return auctionItemService.getFeaturedItems(user);
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<AuctionItem> getItemById(@PathVariable Integer itemId) {
        return new ResponseEntity<>(auctionItemService.getItem(itemId), HttpStatus.OK);
    }

    @GetMapping("/items/{itemId}/bids/highest")
    public ResponseEntity<User> getHighestBidder(@PathVariable Integer itemId) {
        return new ResponseEntity<>(auctionItemService.getHighestBidder(itemId), HttpStatus.OK);
    }

    @GetMapping("/items/{itemId}/bids")
    public Page<Bid> getItemBids(@PathVariable Integer itemId, @PageableDefault(sort = "bidPrice", direction = Sort.Direction.DESC, size = pageSize, page = 0) Pageable pageable) {
        return auctionItemService.getItemBidsPage(itemId, pageable);
    }

    @PutMapping("/items/{itemId}")
    public AuctionItem updateAuctionItem(@PathVariable Integer itemId, @Valid @RequestBody AuctionItem itemUpdated) {
        return auctionItemService.updateItem(itemUpdated);
    }

    @DeleteMapping("/items/{itemId}")
    public void deleteAuctionItem(@PathVariable Integer itemId) {
        auctionItemService.deleteItem(itemId);
    }

    @PutMapping("/items/{itemId}/bids/expired")
    public ResponseEntity<Object> updateItemBids(@PathVariable Integer itemId) {
        return new ResponseEntity<>(auctionItemService.deactivateItemBids(itemId), HttpStatus.OK);
    }

    @PostMapping("/items/{itemId}/payment")
    public ResponseEntity<Location> updatAuctionItem(@PathVariable Integer itemId, @RequestBody Location shippment, HttpServletRequest request) {
        String token = request.getHeader("token");
        Double amount = Double.parseDouble(request.getHeader("amount"));
        try {
            this.stripeClient.chargeCreditCard(token, amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(this.auctionItemService.addShippment(itemId, shippment), HttpStatus.OK);
    }
    @PostMapping("/items/{itemId}/shipping")
    public ResponseEntity<Location> addItemShipping(@PathVariable Integer itemId, @RequestBody Location shippment, HttpServletRequest request) {
        return new ResponseEntity<>(this.auctionItemService.addShippment(itemId, shippment), HttpStatus.OK);
    }

        @GetMapping("/countries")
    public List<Country> getAllCountries() {
        List<Country> countries = new ArrayList<>();
        countryRepository.findAll().forEach(countries::add);
        if (countries.size() > 0)
            return countries;
        else
            throw new NotFoundException("Countries not found!");
    }

    @GetMapping("/countries/{iso3}")
    public List<City> getCountryCities(@PathVariable String iso3) {
        List<City> cities = new ArrayList<>();
        cityRepository.findAllByIso3(iso3).forEach(cities::add);
        if (cities.size() > 0)
            return cities;
        else
            throw new NotFoundException("Cities not found!");
    }
}
