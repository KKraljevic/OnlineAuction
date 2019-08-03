package com.auction.app.repository;

        import com.auction.app.model.AuctionItem;
        import com.auction.app.model.Bid;
        import com.auction.app.model.User;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.Pageable;
        import org.springframework.data.jpa.repository.Modifying;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.CrudRepository;
        import org.springframework.data.repository.query.Param;
        import org.springframework.transaction.annotation.Transactional;

        import java.util.List;
        import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Integer> {
    public Optional<User> findByEmailAndPassword(String email, String password);
    public User findByEmail(String email);

    @Query(value="SELECT EXISTS (SELECT user_id FROM public.users_wishlist WHERE user_id= :userId)", nativeQuery = true)
    public boolean hasWishlist(@Param("userId") Integer id);

    @Modifying
    @Query(value="INSERT INTO users_wishlist(user_id, item_id) VALUES (:userId, :itemId);", nativeQuery = true)
    @Transactional
    public void addToWishlist(@Param("userId") Integer userId,@Param("itemId") Integer itemId);

    @Query("select distinct ai from AuctionItem ai join ai.fans fan where fan.id=?1")
    public Page<AuctionItem> findWishlistItems(Integer id, Pageable pageable);

}
