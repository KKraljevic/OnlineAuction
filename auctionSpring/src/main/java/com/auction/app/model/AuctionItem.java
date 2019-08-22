package com.auction.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auctionItems", schema = "public")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AuctionItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private int id;

    @Column
    @NotNull
    private String name;

    @Column
    private String description;

    @Column
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column
    @NotNull
    private Float startPrice;

    @Column
    @NotNull
    private Float currentPrice;

    @Column(columnDefinition = "boolean default false",nullable = true)
    private boolean paid;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"items"})
    private List<Image> images = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "shipping_id", name="location_id")
    @JsonIgnore
    private Location location;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "shipping_id", name="shipmentLocation_id")
    @JsonIgnore
    private Location shipmentLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties(value = {"items", "userBids", "email", "password"})
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties(value = "items")
    private Category category;

    @OneToMany(mappedBy = "auctionItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"auctionItem"})
    private List<Bid> itemBids = new ArrayList<Bid>();

    @ManyToMany(mappedBy = "wishlist",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<User> fans = new ArrayList<>();


    public AuctionItem() {
    }

    public AuctionItem(int item_id, String name, String description, Date endDate,
                       Float startPrice, Float currentPrice, boolean paid) {
        this.id = item_id;
        this.name = name;
        this.description = description;
        this.endDate = endDate;
        this.startPrice = startPrice;
        this.currentPrice = currentPrice;
        this.paid=paid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Bid> getItemBids() {
        return itemBids;
    }

    public void setItemBids(List<Bid> itemBids) {
        this.itemBids = itemBids;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getShipmentLocation() {
        return shipmentLocation;
    }

    public void setShipmentLocation(Location shipmentLocation) {
        this.shipmentLocation = shipmentLocation;
    }
}

