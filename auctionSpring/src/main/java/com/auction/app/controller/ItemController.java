package com.auction.app.controller;

import com.auction.app.AuctionItemsRepository;
import com.auction.app.BidRepository;
import com.auction.app.CategoryRepository;
import com.auction.app.UserRepository;
import com.auction.app.conf.NotFoundException;
import com.auction.app.model.AuctionItem;
import com.auction.app.model.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@CrossOrigin(origins = "https://still-castle-19196.herokuapp.com")
@RestController
@RequestMapping("/api")
public class ItemController {

    @Autowired
    private AuctionItemsRepository itemRepository ;

    @Autowired
    private BidRepository bidRepository;
    
    @Autowired private EntityLinks links;

    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <PagedResources< AuctionItem >> AllProducts(Pageable pageable, PagedResourcesAssembler assembler) {
        Page < AuctionItem > items = itemRepository.findAll(pageable);
        PagedResources < AuctionItem > pr = assembler.toResource(items, linkTo(ItemController.class).slash("/products").withSelfRel());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", createLinkHeader(pr));
        return new ResponseEntity <> (assembler.toResource(items, linkTo(ItemController.class).slash("/products").withSelfRel()), responseHeaders, HttpStatus.OK);
    }

    private String createLinkHeader(PagedResources < AuctionItem > pr) {
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
    public List<AuctionItem> getAllItems() {
        List<AuctionItem> items = new ArrayList<>();
        itemRepository.findAll().forEach(items::add);
        return items;
    }

    @GetMapping("/featuredItems")
    public List<AuctionItem> getFeaturedItems() {
        List<AuctionItem> items = new ArrayList<>();
        return itemRepository.findAll(PageRequest.of(0,4)).getContent();
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<AuctionItem> getItemById(@PathVariable Integer itemId) {
        if(!itemRepository.existsById(itemId)) {
            throw new NotFoundException("Item not found!");
        }
        return new ResponseEntity<AuctionItem>(itemRepository.findById(itemId).get(), HttpStatus.OK);
    }

    @GetMapping("/items/{itemId}/bids")
    public ResponseEntity<List<Bid>> getItemBids(@PathVariable Integer itemId) {
        List<Bid> bids = new ArrayList<Bid>();
        bidRepository.findByAuctionItemId(itemId).forEach(bids::add);
        if(bids.size()>0)
            return new ResponseEntity<List<Bid>>(bids, HttpStatus.OK);
        else
            throw new NotFoundException("Bids not found!");
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



}
