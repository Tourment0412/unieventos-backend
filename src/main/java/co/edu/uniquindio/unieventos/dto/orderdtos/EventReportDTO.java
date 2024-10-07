package co.edu.uniquindio.unieventos.dto.orderdtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record EventReportDTO(
        @NotBlank(message = "Event ID cannot be empty")
        String eventId,

        String eventName,

        LocalDateTime eventDate,

        @NotNull(message = "Sold by location map cannot be null")
        Map<String, Integer> soldByLocation, // Map with location name and sold quantity

        Map<String, Double> percentageSoldByLocation,

        @NotNull(message = "Total sales cannot be null")
        @Positive(message = "Total sales must be greater than zero")
        BigDecimal totalSales, // Total earned from sales

        @NotNull(message = "Total tickets cannot be null")
        @Positive(message = "Total tickets must be greater than zero")
        BigDecimal totalTickets // Total available tickets for the event
) {}