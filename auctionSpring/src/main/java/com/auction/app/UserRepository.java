package com.auction.app;

import com.auction.app.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Integer> {
    public User findByEmail(String email);
}
