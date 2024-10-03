package co.edu.uniquindio.unieventos.services.implementations;


import co.edu.uniquindio.unieventos.dto.coupondtos.CouponInfoDTO;
import co.edu.uniquindio.unieventos.dto.coupondtos.CreateCouponDTO;
import co.edu.uniquindio.unieventos.dto.coupondtos.UpdateCouponDTO;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import co.edu.uniquindio.unieventos.repositories.CouponRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CouponSevice;
import co.edu.uniquindio.unieventos.util.utilitaryClass;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional()
public class CouponServiceImp implements CouponSevice {

    private final CouponRepo couponRepo;

    public CouponServiceImp(CouponRepo couponRepo) {
        this.couponRepo = couponRepo;
    }

    public boolean existCupon(String name) {
        return couponRepo.findByName(name).isPresent();
    }

    public Coupon getCupon(String id) throws Exception {
        Optional<Coupon> coupon = couponRepo.findById(id);
        if (coupon.isEmpty()) { // Cambié a negación para verificar si el cupón NO está presente
            throw new Exception("Coupon with this id does not exist");
        }
        return coupon.get();
    }

    @Override
    public CouponInfoDTO getInfoCoupon(String id) throws Exception {
        Coupon coupon = getCupon(id);
        return new CouponInfoDTO (
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
    public String createCoupon(CreateCouponDTO coupon) throws Exception {
        if (existCupon(coupon.name())) {
            throw new Exception("Ya existe un cupon con ese nombre");
        }
        Coupon newCoupon=new Coupon();
        newCoupon.setCode(utilitaryClass.generateCode(6));
        newCoupon.setStatus(CouponStatus.AVAILABLE);
        newCoupon.setName(coupon.name());
        newCoupon.setExpirationDate(coupon.expirationDate());
        newCoupon.setType(coupon.type());
        newCoupon.setDiscount(coupon.discount());
        couponRepo.save(newCoupon);
        return "Coupon saved successfully";
    }

    @Override
    public String updateCoupon(UpdateCouponDTO coupon) throws Exception {
        Coupon couponToUpdate=getCupon(coupon.id());
        couponToUpdate.setName(coupon.name());
        couponToUpdate.setExpirationDate(coupon.expirationDate());
        couponToUpdate.setType(coupon.type());
        couponToUpdate.setDiscount(coupon.discount());
        couponRepo.save(couponToUpdate);
        return couponToUpdate.getId();
    }

    @Override
    public String deleteCoupon(String id) throws Exception {
        Coupon couponToDelete=getCupon(id);
        couponToDelete.setStatus(CouponStatus.NOT_AVAILABLE);
        couponRepo.save(couponToDelete);
        return "Account deleted successfully";
    }

    @Override
    public Coupon getCouponById(String id) throws Exception {
        Optional<Coupon> coupon=couponRepo.findById(id);
        return coupon.orElse(null);
    }
}
