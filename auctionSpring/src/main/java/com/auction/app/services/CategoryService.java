package com.auction.app.services;

import com.auction.app.model.AuctionItem;
import com.auction.app.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    List<Category> getCategories();

    List<Category> getSubcategories(Integer id);

    Category getCategoryByName(String name);

    Category getCategoryById(Integer id);

    Page<AuctionItem> getCategoryItemsPage(Integer catId, Pageable pageable);

}
