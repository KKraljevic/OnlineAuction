package com.auction.app.services;

import com.auction.app.exceptions.NotFoundException;
import com.auction.app.model.AuctionItem;
import com.auction.app.model.Category;
import com.auction.app.repository.AuctionItemsRepository;
import com.auction.app.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AuctionItemsRepository itemsRepository;

    @Override
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<Category>();
        categoryRepository.findByParentIsNull().forEach(categories::add);
        if (categories.size() > 0) {
            return categories;
        } else {
            throw new NotFoundException("Categories not found!");
        }
    }

    @Override
    public List<Category> getSubcategories(Integer id) {
        List<Category> subcategories;
        subcategories = recursiveTree(categoryRepository.findById(id).get());
        if (subcategories.size() > 0)
            return subcategories;
        else
            throw new NotFoundException("Subcategories not found!");
    }

    @Override
    public Category getCategoryByName(String name) {
        Category c = categoryRepository.findByCategoryName(name);
        if (c != null) {
            return c;
        } else {
            throw new NotFoundException("Category not found!");
        }
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found!"));
    }

    @Override
    public Page<AuctionItem> getCategoryItemsPage(Integer catId, Pageable pageable) {
        Page<AuctionItem> pageItems;
        pageItems = itemsRepository.findAllCategoryItems(catId, new Date(), pageable);
        if (pageItems.getTotalPages() == 0) {
            throw new NotFoundException("Items in this category not found!");
        } else {
            return pageItems;
        }
    }

    public List<Category> recursiveTree(Category cat) {
        if (cat.getChildren().size() > 0) {
            return cat.getChildren();
        }
        return null;
    }
}
