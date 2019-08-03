package com.auction.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "city", schema = "public")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class City {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "city_id")
    private int id;

    @Column(nullable = false)
    private String iso3;

    @Column
    private String name;

    @Column(name="city_pop")
    private Float population;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iso3", nullable = false)
    @JsonIgnoreProperties(value = {"iso","name","numcode"})
    private Country country;*/

    public City() {
    }

    public City(int id, String iso3, String name, Float population) {
        this.id = id;
        this.iso3 = iso3;
        this.name = name;
        this.population = population;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPopulation() {
        return population;
    }

    public void setPopulation(Float population) {
        this.population = population;
    }
}
