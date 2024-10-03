package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.documents.Account;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CouponRepo extends MongoRepository<Coupon, String> {

    @Query("{_id:  ?0}")
    Optional<Coupon> findById(String code);

    @Query(" {expirationDate: { $gt: new Date() }}")
    Optional<Coupon> getAvailableMultipleCupons();

    @Query("{name: ?0}")
    Optional<Coupon> findByName(String name);

    @Query(" {expirationDate: { $gt: new Date() }}")
    Optional<Coupon> getExpiredCupons();

    @Query(" {name: ?0,type: ?1}")
    Optional<Coupon> findByNameAndType(String name, CouponType type);

    @Query(" $or [{name: ?0},{discount: ?1},{expirationDate: ?2},{type: ?3}]")
    Optional<Coupon> findCoupon(String name, float discount, LocalDateTime expirationDate, CouponType type);

}
