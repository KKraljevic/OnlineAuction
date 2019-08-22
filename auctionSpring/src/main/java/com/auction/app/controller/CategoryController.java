package com.auction.app.controller;


import com.auction.app.repository.AuctionItemsRepository;
import com.auction.app.repository.CategoryRepository;
import com.auction.app.model.AuctionItem;
import com.auction.app.model.Category;
import com.auction.app.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://still-castle-19196.herokuapp.com")
//@CrossOrigin(origins = "http://localhost:4200")

@RequestMapping("/api")
@RestController
public class CategoryController {

    final static int pageSize=9;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AuctionItemsRepository itemsRepository;

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> findAllCategories() {
        return new ResponseEntity<List<Category>>(categoryService.getCategories(), HttpStatus.OK);
    }

    @GetMapping("/category/{name}")
    public Category getCategoryByName(@PathVariable("name") String name) {
        return categoryService.getCategoryByName(name);
    }

    @GetMapping("/categories/{id}")
    public Category getCategory(@PathVariable("id") Integer id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/categories/{id}/items")
    public Page<AuctionItem> getCategoryItems(@PathVariable("id") Integer catId, @PageableDefault(sort = {"id"}, size = pageSize) Pageable pageable) {
       return categoryService.getCategoryItemsPage(catId,pageable);
    }

    @GetMapping("/categories/{id}/children")
    public List<Category> getSubcategories(@PathVariable("id") Integer id) {
       return categoryService.getSubcategories(id);
    }


}
