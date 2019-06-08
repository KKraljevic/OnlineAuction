package com.auction.app.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "auctionItems")
public class AuctionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


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

    public int getId() {
        return id;
    }

    public AuctionItem(){};
    public AuctionItem(int id, String name, String description, int quantity, Date endDate, Float startPrice, Float currentPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.endDate = endDate;
        this.startPrice = startPrice;
        this.currentPrice = currentPrice;
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
}
