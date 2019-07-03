package com.auction.app.controller;


import com.auction.app.AuctionItemsRepository;
import com.auction.app.CategoryRepository;
import com.auction.app.conf.NotFoundException;
import com.auction.app.model.AuctionItem;
import com.auction.app.model.Category;
import com.auction.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "https://still-castle-19196.herokuapp.com")
@RequestMapping("/api")
@RestController
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AuctionItemsRepository itemsRepository;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> findAllCategories() {
        List<Category> categories = new ArrayList<Category>();
        categoryRepository.findByParentIsNull().forEach(categories::add);
        if(categories.size()>0)
            return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
        else
            throw new NotFoundException("Categories not found!");
    }
    @GetMapping("/categories/{id}")
    public Category getCategory(@PathVariable("id") Integer id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found!"));
    }

    @GetMapping("/categories/{id}/items")
    public Page<AuctionItem> getCategoryItems(@PathVariable("id") Integer id, Pageable pageable) {
        Page<AuctionItem> pageItems=itemsRepository.findByCategoryId(id,pageable);
        if(pageItems.getTotalPages()==0)
            throw new NotFoundException("Items in this category not found!");
        else
            return pageItems;
    }

    @GetMapping("/categories/{id}/children")
    public List<Category> getSubcategories(@PathVariable("id") Integer id) {
        List<Category> subcategories = new ArrayList<Category>();
        subcategories=recursiveTree(categoryRepository.findById(id).get());
        if(subcategories.size()>0)
            return subcategories;
        else
            throw new NotFoundException("Subcategories not found!");
    }

    public List<Category> recursiveTree(Category cat) {
        if (cat.getChildren().size() > 0) {
            return cat.getChildren();
        }
        return null;
    }
}
