package co.edu.uniquindio.unieventos.dto.orderdtos;

import co.edu.uniquindio.unieventos.dto.eventdtos.LocationPercentageDTO;
import co.edu.uniquindio.unieventos.dto.eventdtos.LocationQuantityDTO;
import co.edu.uniquindio.unieventos.dto.eventdtos.LocationSalesDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record EventReportDTO(
        List<LocationSalesDTO> soldByLocation, // Map with location name and sold quantity
        List<LocationPercentageDTO> percentageSoldByLocation,
        List<LocationQuantityDTO> quantitySoldByLocation,
        double totalSales, // Total earned from sales
        int totalTickets // Total available tickets for the event
) {}