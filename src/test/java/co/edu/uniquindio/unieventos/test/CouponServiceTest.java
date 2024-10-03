package co.edu.uniquindio.unieventos.test;

import co.edu.uniquindio.unieventos.dto.accountdtos.AccountInfoDTO;
import co.edu.uniquindio.unieventos.dto.coupondtos.CouponInfoDTO;
import co.edu.uniquindio.unieventos.dto.coupondtos.CreateCouponDTO;
import co.edu.uniquindio.unieventos.dto.coupondtos.UpdateCouponDTO;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import co.edu.uniquindio.unieventos.repositories.CouponRepo;
import co.edu.uniquindio.unieventos.services.implementations.CouponServiceImp;
import co.edu.uniquindio.unieventos.services.interfaces.CouponSevice;
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
    private CouponSevice couponService;

    @Autowired
    private CouponRepo couponRepo;

    String couponId="66fb65e7c3ee67460b239bd0";

    @BeforeEach
    void setUp() {
        //couponRepo.deleteAll(); // Limpiar el repositorio antes de cada prueba si es necesario
    }

    @Test
    void testCreateCoupon() throws Exception {
        // Arrange
        CreateCouponDTO createCouponDTO = new CreateCouponDTO(0.12F, LocalDateTime.now(), CouponType.MULTIPLE, "FEST");

        // Verificar que no exista un cupón con el mismo nombre
        Optional<Coupon> existingCoupon = couponRepo.findByName("FEST");
        Assertions.assertTrue(existingCoupon.isEmpty(), "El cupón no debería existir antes de la creación");

        // Act
        String result = couponService.createCoupon(createCouponDTO);

        // Assert
        assertEquals("Coupon saved successfully", result);

        // Verificar que el cupón fue guardado correctamente en el repositorio
        Optional<Coupon> savedCoupon = couponRepo.findByName("FEST");
        Assertions.assertTrue(savedCoupon.isPresent(), "El cupón debería haberse guardado en la base de datos");
        assertEquals(createCouponDTO.name(), savedCoupon.get().getName(), "El nombre del cupón no coincide");
    }

    @Test
    void testUpdateCoupon() throws Exception {
        // Arrange


        // DTO para la actualización
        UpdateCouponDTO updateCouponDTO = new UpdateCouponDTO("66fafd1310b4027d916c95dc", 1F, LocalDateTime.now().plusDays(20), CouponType.UNIQUE, "LOVE");

        // Act
        String result = couponService.updateCoupon(updateCouponDTO);

        // Assert
        assertEquals(updateCouponDTO.id(), result);

        // Verificar que el cupón fue actualizado correctamente
        Optional<Coupon> updatedCoupon = couponRepo.findById(updateCouponDTO.id());
        //Assertions.assertTrue(updatedCoupon.isPresent(), "El cupón debería seguir existiendo en la base de datos");
        assertEquals(updateCouponDTO.name(), updatedCoupon.get().getName(), "El nombre del cupón no coincide");
        assertEquals(updateCouponDTO.discount(), updatedCoupon.get().getDiscount(), "El descuento del cupón no coincide");
    }

    @Test
    void testDeleteCoupon() throws Exception {
        // Arrange
        // Crear un cupón de prueba en la base de datos
        Coupon coupon = new Coupon();
        coupon.setId("123");
        coupon.setStatus(CouponStatus.AVAILABLE);
        couponRepo.save(coupon);

        // Act
        String result = couponService.deleteCoupon("123");

        // Assert
        assertEquals("Account deleted successfully", result);

        // Verificar que el estado del cupón se actualizó a "DELETED"
        Optional<Coupon> deletedCoupon = couponRepo.findById("123");
        Assertions.assertTrue(deletedCoupon.isPresent(), "El cupón debería seguir existiendo en la base de datos");
        assertEquals(CouponStatus.NOT_AVAILABLE, deletedCoupon.get().getStatus(), "El estado del cupón no se actualizó correctamente");
    }

    @Test
    public void testGetInfoCoupon() throws Exception {
        // Arrange


        // Act
        CouponInfoDTO couponInfoDTO = couponService.getInfoCoupon(couponId);

        // Assert
        //TODO Modificar valores de compracion
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
