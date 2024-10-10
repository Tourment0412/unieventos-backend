package co.edu.uniquindio.unieventos.test;

import co.edu.uniquindio.unieventos.dto.coupondtos.*;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import co.edu.uniquindio.unieventos.repositories.CouponRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CouponService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepo couponRepo;

    String couponId="6706047ac127c9d5e7e16cc3";

    @BeforeEach
    void setUp() {

    }

    @Test
    void testCreateCoupon() throws Exception {
        CreateCouponDTO createCouponDTO = new CreateCouponDTO(0.12F, LocalDateTime.now(), CouponType.MULTIPLE, "FEST");

        Optional<Coupon> existingCoupon = couponRepo.findByName("FEST");
        Assertions.assertTrue(existingCoupon.isEmpty(), "El cupón no debería existir antes de la creación");

        String result = couponService.createCoupon(createCouponDTO);
        Optional<Coupon> coupon = couponRepo.findByName("FEST");

        assertEquals(coupon.get().getId(), result);

        Optional<Coupon> savedCoupon = couponRepo.findByName("FEST");
        Assertions.assertTrue(savedCoupon.isPresent(), "El cupón debería haberse guardado en la base de datos");
        assertEquals(createCouponDTO.name(), savedCoupon.get().getName(), "El nombre del cupón no coincide");
    }

    @Test
    void testUpdateCoupon() throws Exception {
        UpdateCouponDTO updateCouponDTO = new UpdateCouponDTO("6706047ac127c9d5e7e16cc4", 1F, LocalDateTime.now().plusDays(20), CouponType.UNIQUE, "LOVE");

        String result = couponService.updateCoupon(updateCouponDTO);

        assertEquals(updateCouponDTO.id(), result);

        Optional<Coupon> updatedCoupon = couponRepo.findById(updateCouponDTO.id());

        assertEquals(updateCouponDTO.name(), updatedCoupon.get().getName(), "El nombre del cupón no coincide");
        assertEquals(updateCouponDTO.discount(), updatedCoupon.get().getDiscount(), "El descuento del cupón no coincide");
    }

    @Test
    void testDeleteCoupon() throws Exception {
         Coupon coupon = new Coupon();
        coupon.setId("67073d3193ff720c4a48b483");
        coupon.setStatus(CouponStatus.AVAILABLE);
        couponRepo.save(coupon);

        String result = couponService.deleteCoupon("67073d3193ff720c4a48b483");

        assertEquals("67073d3193ff720c4a48b483", result);

        Optional<Coupon> deletedCoupon = couponRepo.findById("67073d3193ff720c4a48b483");
        Assertions.assertTrue(deletedCoupon.isPresent(), "El cupón debería seguir existiendo en la base de datos");
        assertEquals(CouponStatus.NOT_AVAILABLE, deletedCoupon.get().getStatus(), "El estado del cupón no se actualizó correctamente");
    }

    @Test
    public void testGetInfoCouponAdmin() throws Exception {
        CouponInfoDTO couponInfoDTO = couponService.getInfoCouponAdmin(couponId);

        assertNotNull(couponInfoDTO);
        assertEquals(couponId, couponInfoDTO.id());
        assertEquals("Discount 10%", couponInfoDTO.name());
        assertEquals("UNIQUE", couponInfoDTO.type().toString());
        assertEquals("AVAILABLE", couponInfoDTO.status().toString());
        assertEquals("DISC10", couponInfoDTO.code());
        assertEquals("2024-12-31T18:59:59", couponInfoDTO.expirationDate().toString());
        assertEquals("0.1", couponInfoDTO.discount()+"");

    }

    //////////////////////////////////////////////
    @Test
    public void testGetAllCouponsAdmin() throws Exception {
        List<CouponItemDTO>  couponsItemsDTO=couponService.getAllCouponsAdmin(0);
        assertTrue(couponsItemsDTO.size()>0 && couponsItemsDTO.size()<=10);
    }

    @Test
    public void testGetAllCouponsClient() throws Exception {
        List<CouponItemClientDTO> couponsItemsClientDTO=couponService.getAllCouponsClient(0);
        assertTrue(couponsItemsClientDTO.size()>0 && couponsItemsClientDTO.size()<=10);
    }

    //TODO metodo
    @Test
    public void testReceiveNotificationFromMercadoPago() throws Exception {
        //Esta funcionalidad no puede ser testeada en una prueba unitaria
        assertTrue(true);
    }



}
