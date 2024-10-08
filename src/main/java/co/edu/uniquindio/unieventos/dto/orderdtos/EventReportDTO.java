package co.edu.uniquindio.unieventos.dto.orderdtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record EventReportDTO(
        Map<String, Double> soldByLocation, // Map with location name and sold quantity
        Map<String, Double> percentageSoldByLocation,
        Map<String, Integer> quantitySoldByLocation,
        double totalSales, // Total earned from sales
        int totalTickets // Total available tickets for the event
) {}