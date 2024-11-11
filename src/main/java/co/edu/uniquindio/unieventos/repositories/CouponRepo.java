package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.documents.Account;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CouponRepo extends MongoRepository<Coupon, String> {

    /**
     * Finds coupon by its id
     * @param code Id of coupon
     * @return Coupon with the specified id
     */
    @Query("{_id:  ?0}")
    Optional<Coupon> findById(String code);

    /**
     * Finds an available coupon
     * @return Coupon
     */
    @Query(" {expirationDate: { $gt: new Date() }}")
    Optional<Coupon> getAvailableMultipleCupons();

    /**
     * Finds coupon by its name
     * @param name Name of coupon
     * @return Coupon with the specified name
     */
    @Query("{'name': ?0}")
    Optional<Coupon> findByName(String name);

    /**
     * Finds an available coupon
     * @return Coupon
     */
    @Query(" {expirationDate: { $gt: new Date() }}")
    Optional<Coupon> getExpiredCupons();

    /**
     * Finds coupon by its name and type
     * @param name Name of coupon
     * @param type Type of coupon
     * @return Coupon with the specified name and type
     */
    @Query(" {'name': ?0,'type': ?1}")
    Optional<Coupon> findByNameAndType(String name, CouponType type);

    /**
     * Finds coupon by its name, discount, expirationDate or type
     * @param name Name of coupon
     * @param discount Discount of coupon
     * @param expirationDate ExpirationDate of coupon
     * @param type Type of coupon
     * @return Coupon with the specified name, discount, expirationDate or type
     */
    @Query(" $or [{name: ?0},{discount: ?1},{expirationDate: ?2},{type: ?3}]")
    Optional<Coupon> findCoupon(String name, float discount, LocalDateTime expirationDate, CouponType type);

    /**
     * Finds a available coupon by its id
     * @param id Id of coupon
     * @return Available coupon with the specified id
     */
    @Query("{ '_id': ?0, 'status': 'AVAILABLE', 'expirationDate': { $gt: new Date() } }")
    Optional<Coupon> findCouponClient(String id);

    /**
     * Finds a available coupon by its code
     * @param code code of coupon
     * @return Available coupon with the specified id
     */
    @Query("{ 'code': ?0, 'status': 'AVAILABLE', 'expirationDate': { $gt: new Date() } }")
    Optional<Coupon> findCouponByCode(String code);

    /**
     * Gets a group of coupons
     * @param pageable The pagination information, including page number and size
     * @return Indicated group of coupons
     */
    @Query("{ 'status': 'AVAILABLE', 'expirationDate': { $gt: new Date() } }")
    Page<Coupon> findAllCouponsClient(Pageable pageable);

    /**
     * Finds coupon by its code
     * @param code Code of coupon
     * @return Coupon with the specified code
     */
    @Query("{'code': ?0}")
    Optional<Coupon> findByCode(String code);
}
