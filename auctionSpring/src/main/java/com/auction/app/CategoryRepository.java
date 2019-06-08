package com.auction.app;

import com.auction.app.model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category,Integer> {
    public List<Category> findByParentIsNull();
    public List<Category> findByParent(Category parent);
}
