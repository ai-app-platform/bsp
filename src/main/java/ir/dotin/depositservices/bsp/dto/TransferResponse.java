package ir.dotin.depositservices.bsp.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TransferResponse(
    Long id, Long sourceAccountId, String sourceAccountNumber,
    Long destinationAccountId, String destinationAccountNumber,
    BigDecimal amount, String currency, String description, String status, Instant createdAt
) {}
