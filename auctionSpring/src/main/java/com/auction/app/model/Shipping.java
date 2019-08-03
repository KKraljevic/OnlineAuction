package com.auction.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "shipping", schema = "public")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Shipping {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "shipping_id")
    private int id;

    @Column
    private String address;

    @Column
    private String zipcode;

    @Column
    private boolean freeShipping;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    @JsonIgnoreProperties(value = {"population"})
    private City city;

    //add optional = false
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @JsonIgnore
    private AuctionItem shippmentItem;

    public Shipping() {
    }

    public Shipping(int id, String address, String zipcode, boolean freeShipping) {
        this.id = id;
        this.address = address;
        this.zipcode = zipcode;
        this.freeShipping = freeShipping;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public boolean isFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public AuctionItem getShippmentItem() {
        return shippmentItem;
    }

    public void setShippmentItem(AuctionItem shippmentItem) {
        this.shippmentItem = shippmentItem;
    }
}
