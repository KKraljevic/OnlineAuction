package com.auction.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auctionItems")
public class AuctionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int item_id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private int quantity;

    @Column
    private Date endDate;

    @Column
    private Float startPrice;

    @Column
    private Float currentPrice;

    @ManyToOne(optional = true)
    @JoinColumn(name ="id")
    @JsonIgnoreProperties(value = "items")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties(value = "items")
    private Category category;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "item",cascade = CascadeType.ALL )
    @JsonIgnoreProperties("item")
    private List<Bid> bids = new ArrayList<Bid>();

    public AuctionItem(){};

    public AuctionItem(int item_id, String name, String description, int quantity, Date endDate,
                       Float startPrice, Float currentPrice, User user, Category category, List<Bid> bids) {
        this.item_id = item_id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.endDate = endDate;
        this.startPrice = startPrice;
        this.currentPrice = currentPrice;
        this.user = user;
        this.category = category;
        this.bids = bids;
    }

    public int getId() {
        return item_id;
    }

    public void setId(int id) {
        this.item_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Float getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(Float startPrice) {
        this.startPrice = startPrice;
    }

    public Float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }
}
