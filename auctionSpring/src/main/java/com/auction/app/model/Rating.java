package com.auction.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "rating", schema = "public")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rating_id")
    private int id;

    @Column
    private int value;

    @Column(name = "bidder_id")
    private int bidder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @JsonIgnoreProperties(value = {"userBids","items","email","password"})
    private User ratedSeller;

    public Rating() {

    }

    public Rating(int id, int value, int bidder) {
        this.id = id;
        this.value = value;
        this.bidder = bidder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getBidder() {
        return bidder;
    }

    public void setBidder(int bidder) {
        this.bidder = bidder;
    }

    public User getRatedSeller() {
        return ratedSeller;
    }

    public void setRatedSeller(User ratedSeller) {
        this.ratedSeller = ratedSeller;
    }
}
