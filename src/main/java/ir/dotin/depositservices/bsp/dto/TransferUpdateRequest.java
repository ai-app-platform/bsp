package ir.dotin.depositservices.bsp.dto;

import jakarta.validation.constraints.Size;

public record TransferUpdateRequest(
    @Size(max = 250) String description
) {}
