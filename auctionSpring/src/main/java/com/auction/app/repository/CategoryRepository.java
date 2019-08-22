package com.auction.app.repository;

import com.auction.app.model.AuctionItem;
import com.auction.app.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    public List<Category> findByParentIsNull();

    public Category findByCategoryName(String name);
}
