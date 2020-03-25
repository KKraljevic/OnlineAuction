package com.auction.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Auditable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "auctionItems")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AuctionItem implements Serializable{
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
    private int quantity;

    @Column
    @NotNull
    private Date endDate;

    @Column
    @NotNull
    private Float startPrice;

    @Column
    @NotNull
    private Float currentPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties(value = {"items","userBids","email","password"})
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties(value = "items")
    private Category category;

    @OneToMany(mappedBy = "auctionItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"auctionItem"})
    private List<Bid> itemBids = new ArrayList<Bid>();

    public AuctionItem(){
    };

    public AuctionItem(int item_id, String name, String description, int quantity, Date endDate,
                       Float startPrice, Float currentPrice) {
        this.id = item_id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.endDate = endDate;
        this.startPrice = startPrice;
        this.currentPrice = currentPrice;
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

    public User getSeller() { return seller; }

    public void setSeller(User seller) { this.seller = seller; }

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }

    public List<Bid> getItemBids() {
        return itemBids;
    }

    public void setItemBids(List<Bid> itemBids) {
        this.itemBids = itemBids;
    }

}
