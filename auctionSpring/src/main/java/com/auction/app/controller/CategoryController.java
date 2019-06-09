package com.auction.app.controller;


import com.auction.app.CategoryRepository;
import com.auction.app.model.Category;
import com.auction.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> findAllCategories() {
        List<Category> categories = new ArrayList<Category>();
        categoryRepository.findByParentIsNull().forEach(categories::add);
        return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
    }
    @GetMapping("/categories/{id}")
    public Category getCategory(@PathVariable("id") Integer id) {
        return categoryRepository.findById(id).get();
    }

    @GetMapping("/categories/{id}/children")
    public List<Category> getSubcategories(@PathVariable("id") Integer id) {
        List<Category> subcategories = new ArrayList<Category>();
        subcategories=recursiveTree(categoryRepository.findById(id).get());
        return subcategories;
    }

    public List<Category> recursiveTree(Category cat) {
        if (cat.getChildren().size() > 0) {
            return cat.getChildren();
        }
        return null;
    }
}