package ir.dotin.depositservices.bsp.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record AccountCreateRequest(
    @NotBlank @Size(max = 40) @Pattern(regexp = "[A-Za-z0-9_-]+") String accountNumber,
    @NotBlank @Size(max = 120) String holderName,
    @NotBlank @Pattern(regexp = "[A-Za-z]{3}") String currency,
    @NotNull @DecimalMin(value = "0.00") @Digits(integer = 17, fraction = 2) BigDecimal initialBalance
) {}
