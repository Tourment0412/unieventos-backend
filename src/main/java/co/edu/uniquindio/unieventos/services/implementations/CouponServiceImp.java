package co.edu.uniquindio.unieventos.services.implementations;


import co.edu.uniquindio.unieventos.dto.coupondtos.*;
import co.edu.uniquindio.unieventos.exceptions.DuplicateResourceException;
import co.edu.uniquindio.unieventos.exceptions.OperationNotAllowedException;
import co.edu.uniquindio.unieventos.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.documents.Order;
import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import co.edu.uniquindio.unieventos.repositories.CouponRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CouponService;
import co.edu.uniquindio.unieventos.services.interfaces.OrderService;
import co.edu.uniquindio.unieventos.util.utilitaryClass;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class CouponServiceImp implements CouponService {

    private final CouponRepo couponRepo;
    private final OrderService orderService;


    public CouponServiceImp(CouponRepo couponRepo, @Lazy OrderService orderService) {
        this.couponRepo = couponRepo;
        this.orderService = orderService;
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
        couponToUpdate.setStatus(coupon.status());

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
    public ListCoupon getAllCouponsAdmin(int page){
        Page<Coupon> coupons = couponRepo.findAll(PageRequest.of(page, 9));
        List<CouponItemDTO>couponsList= coupons.stream().map(e -> new CouponItemDTO(
                e.getId(),
                e.getName(),
                e.getCode(),
                e.getType(),
                e.getStatus(),
                e.getExpirationDate(),
                e.getDiscount()
        )).collect(Collectors.toList());

        return new ListCoupon(coupons.getTotalPages(), couponsList);
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
    public CouponInfoClientDTO getCouponClientCode(ValideCouponDTO valideCouponDTO) throws ResourceNotFoundException, OperationNotAllowedException {
        Optional<Coupon> couponOpt = couponRepo.findCouponByCode(valideCouponDTO.codeCoupon());

        if (couponOpt.isEmpty()) {
            throw new ResourceNotFoundException("Coupon with this code does not exist or is not available");
        }
        Coupon coupon = couponOpt.get();

        if (orderService.hasClientUsedCoupon(valideCouponDTO.idUser(), coupon.getId())) {
            throw new OperationNotAllowedException("You can't use a coupon you previously used");
        }

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
    public List<CouponType> getCouponTypes() throws Exception {
        List<CouponType> couponTypes = Arrays.asList(CouponType.values());
        if (couponTypes.isEmpty()) {
            throw new Exception("No coupon types available");
        }
        return couponTypes;
    }

    @Override
    public List<CouponStatus> getCouponStatuses() throws Exception {
        List<CouponStatus> couponStatuses = Arrays.asList(CouponStatus.values());
        if (couponStatuses.isEmpty()) {
            throw new Exception("No coupon statuses available");
        }
        return couponStatuses;
    }

    @Override
    public ListCouponsClient getAllCouponsClient(int page){
        Page<Coupon> coupons = couponRepo.findAllCouponsClient(PageRequest.of(page, 9));

        List<CouponInfoClientDTO> couponsClient= coupons.stream().map(e -> new CouponInfoClientDTO(
                e.getId(),
                e.getName(),
                e.getType(),
                e.getCode(),
                e.getExpirationDate(),
                e.getDiscount()
        )).collect(Collectors.toList());

        return new ListCouponsClient(coupons.getTotalPages(),couponsClient);
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
