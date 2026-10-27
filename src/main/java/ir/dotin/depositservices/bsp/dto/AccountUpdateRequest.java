package ir.dotin.depositservices.bsp.dto;

import jakarta.validation.constraints.*;

public record AccountUpdateRequest(
    @NotBlank @Size(max = 120) String holderName
) {}
