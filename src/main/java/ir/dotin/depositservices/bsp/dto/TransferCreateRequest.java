package ir.dotin.depositservices.bsp.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record TransferCreateRequest(
    @NotNull @Positive Long sourceAccountId,
    @NotNull @Positive Long destinationAccountId,
    @NotNull @DecimalMin(value = "0.01") @Digits(integer = 17, fraction = 2) BigDecimal amount,
    @Size(max = 250) String description
) {}
