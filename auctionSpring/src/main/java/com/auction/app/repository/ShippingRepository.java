package com.auction.app.repository;

import com.auction.app.model.Location;
import org.springframework.data.repository.CrudRepository;

public interface ShippingRepository extends CrudRepository<Location, Integer> {
}
