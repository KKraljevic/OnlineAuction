package com.auction.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private int id;

    @Column
    @NotNull
    private String email;

    @Column
    @NotNull
    private String password;

    @Column(name="firstname")
    @NotNull
    private String firstName;

    @Column(name="lastname")
    @NotNull
    private String lastName;

    @Column
    private String photo;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value={"seller","category","description"})
    private List<AuctionItem> items = new ArrayList<AuctionItem>();

    @OneToMany(mappedBy = "bidder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"bidder"})
    private List<Bid> userBids = new ArrayList<Bid>();

    public User(){}

    public User(int id, String email, String password, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(int id, @NotNull String email, @NotNull String password, @NotNull String firstName, @NotNull String lastName, String photo) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

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

    public List<Bid> getUserBids() {
        return userBids;
    }

    public void setUserBids(List<Bid> userBids) {
        this.userBids = userBids;
    }

    public String getPhoto() { return photo; }

    public void setPhoto(String photo) { this.photo = photo; }
}

