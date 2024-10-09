package co.edu.uniquindio.unieventos.services.implementations;


import co.edu.uniquindio.unieventos.dto.coupondtos.*;
import co.edu.uniquindio.unieventos.exceptions.DuplicateResourceException;
import co.edu.uniquindio.unieventos.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.repositories.CouponRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CouponService;
import co.edu.uniquindio.unieventos.util.utilitaryClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class CouponServiceImp implements CouponService {

    private final CouponRepo couponRepo;

    public CouponServiceImp(CouponRepo couponRepo) {
        this.couponRepo = couponRepo;
    }

    public boolean existCupon(String name) {
        return couponRepo.findByName(name).isPresent();
    }

    public Coupon getCupon(String id) throws ResourceNotFoundException {
        Optional<Coupon> coupon = couponRepo.findById(id);
        if (coupon.isEmpty()) { // Cambié a negación para verificar si el cupón NO está presente
            throw new ResourceNotFoundException("Coupon with this id does not exist");
        }
        return coupon.get();
    }

    @Override
    public CouponInfoDTO getInfoCouponAdmin(String id) throws ResourceNotFoundException {
        Coupon coupon = getCupon(id);
        return new CouponInfoDTO(
                coupon.getId(),
                coupon.getName(),
                coupon.getType(),
                coupon.getStatus(),
                coupon.getCode(),
                coupon.getExpirationDate(),
                coupon.getDiscount()
        );
    }

    @Override
    public String createCoupon(CreateCouponDTO coupon) throws DuplicateResourceException {
        if (existCupon(coupon.name())) {
            throw new DuplicateResourceException("A coupon with that name already exists");
        }
        Coupon newCoupon = new Coupon();
        newCoupon.setCode(utilitaryClass.generateCode(6));
        newCoupon.setStatus(CouponStatus.AVAILABLE);
        newCoupon.setName(coupon.name());
        newCoupon.setExpirationDate(coupon.expirationDate());
        newCoupon.setType(coupon.type());
        newCoupon.setDiscount(coupon.discount());

        return couponRepo.save(newCoupon).getId();
    }

    @Override
    public String updateCoupon(UpdateCouponDTO coupon) throws ResourceNotFoundException {
        Coupon couponToUpdate = getCupon(coupon.id());
        couponToUpdate.setName(coupon.name());
        couponToUpdate.setExpirationDate(coupon.expirationDate());
        couponToUpdate.setType(coupon.type());
        couponToUpdate.setDiscount(coupon.discount());

        return couponRepo.save(couponToUpdate).getId();
    }

    @Override
    public String deleteCoupon(String id) throws ResourceNotFoundException {
        Coupon couponToDelete = getCupon(id);
        couponToDelete.setStatus(CouponStatus.NOT_AVAILABLE);
        return couponRepo.save(couponToDelete).getId();
    }

    @Override
    public Coupon getCouponById(String id) throws ResourceNotFoundException {
        Optional<Coupon> coupon = couponRepo.findById(id);
        if (coupon.isEmpty()) {
            throw new ResourceNotFoundException("Coupon with this id does not exist");
        }
        return coupon.get();
    }

    @Override
    public List<CouponItemDTO> getAllCouponsAdmin(int page){
        List<Coupon> coupons = couponRepo.findAll(PageRequest.of(page, 9)).getContent();
        return coupons.stream().map(e -> new CouponItemDTO(
                e.getId(),
                e.getName(),
                e.getType(),
                e.getStatus(),
                e.getExpirationDate(),
                e.getDiscount()
        )).collect(Collectors.toList());
    }


    @Override
    public CouponInfoClientDTO getCouponClient(String id) throws ResourceNotFoundException {
        Optional<Coupon> couponOpt = couponRepo.findCouponClient(id);
        if (couponOpt.isEmpty()) {
            throw new ResourceNotFoundException("Coupon with this id does not exist or is not available");
        }
        Coupon coupon = couponOpt.get();
        return new CouponInfoClientDTO(
                coupon.getId(),
                coupon.getName(),
                coupon.getType(),
                coupon.getCode(),
                coupon.getExpirationDate(),
                coupon.getDiscount()
        );
    }

    @Override
    public List<CouponItemClientDTO> getAllCouponsClient(int page){
        List<Coupon> coupons = couponRepo.findAllCouponsClient(PageRequest.of(page, 9)).getContent();
        return coupons.stream().map(e -> new CouponItemClientDTO(
                e.getId(),
                e.getName(),
                e.getType(),
                e.getExpirationDate(),
                e.getDiscount()
        )).collect(Collectors.toList());
    }

    @Override
    public Coupon getCouponByCode(String code) throws ResourceNotFoundException {
        Optional<Coupon> coupon = couponRepo.findByCode(code);
        if (coupon.isEmpty()){
            throw new ResourceNotFoundException("Coupon Code Not Registered");
        }
        return coupon.get();
    }


}
