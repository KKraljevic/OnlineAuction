package com.auction.app.repository;

import com.auction.app.model.City;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CityRepository extends CrudRepository<City, Integer> {
    public List<City> findAllByIso3(String iso3);
}
