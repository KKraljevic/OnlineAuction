package com.auction.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name =  "password")
    private String password;

    @Column(name="firstname")
    private String firstName;

    @Column(name="lastname")
    private String lastName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnoreProperties("user")
    private List<AuctionItem> items = new ArrayList<AuctionItem>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Bid> bids = new ArrayList<Bid>();

    public User(){}

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName=firstName;
        this.lastName=lastName;
    }

    public User(int id, String email, String password, String firstName, String lastName, List<AuctionItem> items, List<Bid> bids) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.items = items;
        this.bids = bids;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public List<AuctionItem> getItems() { return items; }

    public void setItems(List<AuctionItem> items) { this.items = items; }

    public List<Bid> getBids() { return bids; }

    public void setBids(List<Bid> bids) { this.bids = bids; }

    @Override
    public String toString() {
        return String.format("User[id=%d, email='%s', password='%s',firstName='%s',lastName='%s']", id, email, password,firstName,lastName);
    }
}

