package com.auction.app;

import com.auction.app.model.Bid;
import com.auction.app.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Integer> {
    public Optional<User> findByEmailAndPassword(String email, String password);
    public User findByEmail(String email);
}
