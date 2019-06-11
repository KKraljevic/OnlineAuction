package com.auction.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bids")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bid_Id;
    @Column
    private float bidPrice;
    @Column
    private Date bidTime;
    @Column
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "id")
    @JsonIgnoreProperties("bids")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @JsonIgnoreProperties("bids")
    private AuctionItem item;

    public Bid(){};

    public Bid(int bid_Id, float bidPrice, Date bidTime, boolean isActive, User user, AuctionItem item) {
        this.bid_Id = bid_Id;
        this.bidPrice = bidPrice;
        this.bidTime = bidTime;
        this.isActive = isActive;
        this.user = user;
        this.item = item;
    }

    public int getBid_Id() {
        return bid_Id;
    }

    public void setBid_Id(int bid_Id) {
        this.bid_Id = bid_Id;
    }

    public float getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(float bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Date getBidTime() {
        return bidTime;
    }

    public void setBidTime(Date bidTime) {
        this.bidTime = bidTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AuctionItem getItem() {
        return item;
    }

    public void setItem(AuctionItem item) {
        this.item = item;
    }
}
