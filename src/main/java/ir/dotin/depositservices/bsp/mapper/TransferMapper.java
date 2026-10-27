package ir.dotin.depositservices.bsp.mapper;

import ir.dotin.depositservices.bsp.config.MapperConfig;
import ir.dotin.depositservices.bsp.dto.TransferResponse;
import ir.dotin.depositservices.bsp.entity.MoneyTransfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface TransferMapper {
    @Mapping(target = "sourceAccountId", source = "sourceAccount.id")
    @Mapping(target = "sourceAccountNumber", source = "sourceAccount.accountNumber")
    @Mapping(target = "destinationAccountId", source = "destinationAccount.id")
    @Mapping(target = "destinationAccountNumber", source = "destinationAccount.accountNumber")
    TransferResponse toResponse(MoneyTransfer transfer);
}
