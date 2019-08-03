package com.auction.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "country", schema = "public")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Country {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false)
    private String iso3;

    @Column(nullable = false)
    private String iso;

    @Column(nullable = false)
    private String name;

    @Column(name = "printable_name", nullable = false)
    private String fullName;

    @Column
    private Integer numcode;

    public Country() { };

    public Country(String iso3, String iso, String name, String fullName, Integer numcode) {
        this.iso3 = iso3;
        this.iso = iso;
        this.name = name;
        this.fullName = fullName;
        this.numcode = numcode;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getNumcode() {
        return numcode;
    }

    public void setNumcode(Integer numcode) {
        this.numcode = numcode;
    }
}
