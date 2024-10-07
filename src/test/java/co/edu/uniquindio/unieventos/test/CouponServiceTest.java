package co.edu.uniquindio.unieventos.test;

import co.edu.uniquindio.unieventos.dto.coupondtos.CouponInfoDTO;
import co.edu.uniquindio.unieventos.dto.coupondtos.CreateCouponDTO;
import co.edu.uniquindio.unieventos.dto.coupondtos.UpdateCouponDTO;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepo couponRepo;

    String couponId="66fb65e7c3ee67460b239bd0";

    @BeforeEach
    void setUp() {

    }

    @Test
    void testCreateCoupon() throws Exception {
        CreateCouponDTO createCouponDTO = new CreateCouponDTO(0.12F, LocalDateTime.now(), CouponType.MULTIPLE, "FEST");

        Optional<Coupon> existingCoupon = couponRepo.findByName("FEST");
        Assertions.assertTrue(existingCoupon.isEmpty(), "El cupón no debería existir antes de la creación");

        String result = couponService.createCoupon(createCouponDTO);

        assertEquals("Coupon saved successfully", result);

        Optional<Coupon> savedCoupon = couponRepo.findByName("FEST");
        Assertions.assertTrue(savedCoupon.isPresent(), "El cupón debería haberse guardado en la base de datos");
        assertEquals(createCouponDTO.name(), savedCoupon.get().getName(), "El nombre del cupón no coincide");
    }

    @Test
    void testUpdateCoupon() throws Exception {
        UpdateCouponDTO updateCouponDTO = new UpdateCouponDTO("66fafd1310b4027d916c95dc", 1F, LocalDateTime.now().plusDays(20), CouponType.UNIQUE, "LOVE");

        String result = couponService.updateCoupon(updateCouponDTO);

        assertEquals(updateCouponDTO.id(), result);

        Optional<Coupon> updatedCoupon = couponRepo.findById(updateCouponDTO.id());

        assertEquals(updateCouponDTO.name(), updatedCoupon.get().getName(), "El nombre del cupón no coincide");
        assertEquals(updateCouponDTO.discount(), updatedCoupon.get().getDiscount(), "El descuento del cupón no coincide");
    }

    @Test
    void testDeleteCoupon() throws Exception {
         Coupon coupon = new Coupon();
        coupon.setId("123");
        coupon.setStatus(CouponStatus.AVAILABLE);
        couponRepo.save(coupon);

        String result = couponService.deleteCoupon("123");

        assertEquals("Account deleted successfully", result);

        Optional<Coupon> deletedCoupon = couponRepo.findById("123");
        Assertions.assertTrue(deletedCoupon.isPresent(), "El cupón debería seguir existiendo en la base de datos");
        assertEquals(CouponStatus.NOT_AVAILABLE, deletedCoupon.get().getStatus(), "El estado del cupón no se actualizó correctamente");
    }

    @Test
    public void testGetInfoCouponAdmin() throws Exception {
        CouponInfoDTO couponInfoDTO = couponService.getInfoCouponAdmin(couponId);

        assertNotNull(couponInfoDTO);
        assertEquals(couponId, couponInfoDTO.id());
        assertEquals("FEST", couponInfoDTO.name());
        assertEquals("MULTIPLE", couponInfoDTO.type().toString());
        assertEquals("AVAILABLE", couponInfoDTO.status().toString());
        assertEquals("97FGNY", couponInfoDTO.code());
        assertEquals("2024-09-30T22:00:55.810", couponInfoDTO.expirationDate().toString());
        assertEquals("0.12", couponInfoDTO.discount()+"");

    }
}
