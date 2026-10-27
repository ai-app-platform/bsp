package ir.dotin.depositservices.bsp.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record AccountResponse(
    Long id, String accountNumber, String holderName, String currency,
    BigDecimal balance, Instant createdAt
) {}
