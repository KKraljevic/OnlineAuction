package com.auction.app.controller;

import com.auction.app.conf.NotFoundException;
import com.auction.app.model.AuctionItem;
import com.auction.app.model.Bid;
import com.auction.app.model.City;
import com.auction.app.model.Country;
import com.auction.app.repository.AuctionItemsRepository;
import com.auction.app.repository.BidRepository;
import com.auction.app.repository.CityRepository;
import com.auction.app.repository.CountryRepository;
import com.auction.app.services.FileService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.http.HttpResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@CrossOrigin(origins = "https://still-castle-19196.herokuapp.com")
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ItemController {

    @Autowired
    private AuctionItemsRepository itemRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private EntityLinks links;

    private final FileService fileService;

    @Autowired
    public ItemController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/uploadImage")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String imgURL;
        try {
            imgURL = fileService.storeFileCloudinary(file);
            JSONObject jo = new JSONObject();
            jo.put("url", imgURL);
            return new ResponseEntity<String>(imgURL, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("Image could not be uploaded!");
        }
    }

    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResources<AuctionItem>> AllProducts(Pageable pageable, PagedResourcesAssembler assembler) {
        Page<AuctionItem> items = itemRepository.findAll(pageable);
        PagedResources<AuctionItem> pr = assembler.toResource(items, linkTo(ItemController.class).slash("/products").withSelfRel());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", createLinkHeader(pr));
        return new ResponseEntity<>(assembler.toResource(items, linkTo(ItemController.class).slash("/products").withSelfRel()), responseHeaders, HttpStatus.OK);
    }

    private String createLinkHeader(PagedResources<AuctionItem> pr) {
        final StringBuilder linkHeader = new StringBuilder();
        linkHeader.append(buildLinkHeader(pr.getLinks("first").get(0).getHref(), "first"));
        linkHeader.append(", ");
        linkHeader.append(buildLinkHeader(pr.getLinks("next").get(0).getHref(), "next"));
        return linkHeader.toString();
    }

    public static String buildLinkHeader(final String uri, final String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }


    @GetMapping("/items")
    public Page<AuctionItem> getAllItems(@PageableDefault(sort = "id", size = 9, page = 0) Pageable pageable,
                                         @RequestParam(value = "user", defaultValue = "0", required = false) Integer user) {
        Page<AuctionItem> items;
        if (user != 0) {
            items = itemRepository.findFirst4ShopItems(user, new Date(),
                    PageRequest.of(0, 4, new Sort(Sort.Direction.ASC, "endDate")));
        } else {
            items = itemRepository.findAllShopItems(new Date(), pageable);

        }
        return items;
    }

    @GetMapping("/search")
    public Page<AuctionItem> getSearchItems(@PageableDefault(sort = "id", size = 9, page = 0) Pageable pageable,
                                            @RequestParam(value = "search", defaultValue = "") String search) {
        Page<AuctionItem> items;
        items = itemRepository.findAllSearchItems(search, new Date(), pageable);
        return items;
    }

    @GetMapping("/featuredItems")
    public List<AuctionItem> getFeaturedItems(@RequestParam(value = "user", defaultValue = "0", required = false) Integer user) {
        List<AuctionItem> items = new ArrayList<>();
        if (user != 0) {
            return itemRepository.findFirst4ShopItems(user, new Date(), PageRequest.of(0, 4, Sort.by("id").descending())).getContent();
        } else {
            return itemRepository.findFirst4Items(new Date(), PageRequest.of(0, 4, Sort.by("id").descending())).getContent();
        }
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<AuctionItem> getItemById(@PathVariable Integer itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException("Item not found!");
        }
        return new ResponseEntity<AuctionItem>(itemRepository.findById(itemId).get(), HttpStatus.OK);
    }

    @GetMapping("/items/{itemId}/bids")
    public Page<Bid> getItemBids(@PathVariable Integer itemId, @PageableDefault(sort = "bidPrice",direction = Sort.Direction.DESC,size = 9, page = 0) Pageable pageable) {
        Page<Bid> bids;
        bids = bidRepository.findByAuctionItemId(itemId, pageable);
        return bids;
    }

    @PutMapping("/items/{itemId}")
    public AuctionItem updateAuctionItem(@PathVariable Integer itemId, @Valid @RequestBody AuctionItem itemUpdated) {

        return itemRepository.findById(itemId)
                .map(item -> {
                    item.setCurrentPrice(itemUpdated.getCurrentPrice());
                    item.setDescription(itemUpdated.getDescription());
                    item.setEndDate(itemUpdated.getEndDate());
                    item.setStartPrice(itemUpdated.getStartPrice());
                    return itemRepository.save(item);
                }).orElseThrow(() -> new NotFoundException("Auction item not found!"));
    }

    @DeleteMapping("/items/{itemId}")
    public String deleteAuctionItem(@PathVariable Integer itemId) {

        return itemRepository.findById(itemId)
                .map(item -> {
                    itemRepository.delete(item);
                    return "Deleted Successfully!";
                }).orElseThrow(() -> new NotFoundException("Auction item not found!"));
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
