package ir.dotin.depositservices.bsp.service;

import ir.dotin.depositservices.bsp.dto.TransferCreateRequest;
import ir.dotin.depositservices.bsp.dto.TransferResponse;
import ir.dotin.depositservices.bsp.dto.TransferUpdateRequest;
import ir.dotin.depositservices.bsp.entity.Account;
import ir.dotin.depositservices.bsp.entity.MoneyTransfer;
import ir.dotin.depositservices.bsp.exception.BusinessRuleException;
import ir.dotin.depositservices.bsp.exception.ConflictException;
import ir.dotin.depositservices.bsp.exception.ResourceNotFoundException;
import ir.dotin.depositservices.bsp.mapper.TransferMapper;
import ir.dotin.depositservices.bsp.repository.AccountRepository;
import ir.dotin.depositservices.bsp.repository.MoneyTransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransferService {
    private final AccountRepository accountRepository;
    private final MoneyTransferRepository transferRepository;
    private final TransferMapper mapper;

    public TransferService(AccountRepository accountRepository,
                           MoneyTransferRepository transferRepository,
                           TransferMapper mapper) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.mapper = mapper;
    }

    @Transactional
    public TransferResponse create(TransferCreateRequest request) {
        if (request.sourceAccountId().equals(request.destinationAccountId())) {
            throw new BusinessRuleException("Source and destination accounts must be different.");
        }

        // Lock rows in ascending ID order to reduce deadlock risk for concurrent opposite transfers.
        Long firstId = Math.min(request.sourceAccountId(), request.destinationAccountId());
        Long secondId = Math.max(request.sourceAccountId(), request.destinationAccountId());
        Account first = accountRepository.findByIdForUpdate(firstId)
            .orElseThrow(() -> new ResourceNotFoundException("Account " + firstId + " was not found."));
        Account second = accountRepository.findByIdForUpdate(secondId)
            .orElseThrow(() -> new ResourceNotFoundException("Account " + secondId + " was not found."));

        Account source = first.getId().equals(request.sourceAccountId()) ? first : second;
        Account destination = first.getId().equals(request.destinationAccountId()) ? first : second;

        if (!source.getCurrency().equalsIgnoreCase(destination.getCurrency())) {
            throw new BusinessRuleException("Source and destination accounts must use the same currency.");
        }
        if (source.getBalance().compareTo(request.amount()) < 0) {
            throw new BusinessRuleException("Insufficient funds.");
        }

        source.setBalance(source.getBalance().subtract(request.amount()));
        destination.setBalance(destination.getBalance().add(request.amount()));
        accountRepository.save(source);
        accountRepository.save(destination);

        MoneyTransfer transfer = new MoneyTransfer();
        transfer.setSourceAccount(source);
        transfer.setDestinationAccount(destination);
        transfer.setAmount(request.amount());
        transfer.setCurrency(source.getCurrency());
        transfer.setDescription(request.description() == null ? null : request.description().trim());
        transfer.setStatus("COMPLETED");
        return mapper.toResponse(transferRepository.save(transfer));
    }

    @Transactional(readOnly = true)
    public TransferResponse get(Long id) {
        return mapper.toResponse(findTransfer(id));
    }

    @Transactional(readOnly = true)
    public List<TransferResponse> getAll() {
        return transferRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public TransferResponse update(Long id, TransferUpdateRequest request) {
        MoneyTransfer transfer = findTransfer(id);
        ensureMutable(transfer);
        transfer.setDescription(request.description() == null ? null : request.description().trim());
        return mapper.toResponse(transferRepository.save(transfer));
    }

    @Transactional
    public void delete(Long id) {
        MoneyTransfer transfer = findTransfer(id);
        ensureMutable(transfer);
        transferRepository.delete(transfer);
    }

    private MoneyTransfer findTransfer(Long id) {
        return transferRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transfer " + id + " was not found."));
    }

    private void ensureMutable(MoneyTransfer transfer) {
        if ("COMPLETED".equals(transfer.getStatus())) {
            throw new ConflictException("Completed transfers are immutable. Use a compensating transfer instead.");
        }
    }
}
