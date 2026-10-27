package ir.dotin.depositservices.bsp.repository;

import ir.dotin.depositservices.bsp.entity.MoneyTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyTransferRepository extends JpaRepository<MoneyTransfer, Long> {
    boolean existsBySourceAccountIdOrDestinationAccountId(Long sourceAccountId, Long destinationAccountId);
}
