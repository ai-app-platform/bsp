package ir.dotin.depositservices.bsp.mapper;

import ir.dotin.depositservices.bsp.config.MapperConfig;
import ir.dotin.depositservices.bsp.dto.AccountCreateRequest;
import ir.dotin.depositservices.bsp.dto.AccountResponse;
import ir.dotin.depositservices.bsp.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface AccountMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "balance", source = "initialBalance")
    @Mapping(target = "currency", expression = "java(request.currency().toUpperCase(java.util.Locale.ROOT))")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Account toEntity(AccountCreateRequest request);

    AccountResponse toResponse(Account account);
}
