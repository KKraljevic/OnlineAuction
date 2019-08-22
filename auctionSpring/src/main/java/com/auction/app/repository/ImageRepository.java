package com.auction.app.repository;

import com.auction.app.model.Image;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImageRepository extends CrudRepository<Image, Integer> {
    public List<Image> findAllByItemId(Integer id);
}
