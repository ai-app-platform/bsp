package ir.dotin.depositservices.bsp.service;

import ir.dotin.depositservices.bsp.dto.*;
import ir.dotin.depositservices.bsp.entity.Account;
import ir.dotin.depositservices.bsp.exception.ConflictException;
import ir.dotin.depositservices.bsp.exception.ResourceNotFoundException;
import ir.dotin.depositservices.bsp.mapper.AccountMapper;
import ir.dotin.depositservices.bsp.repository.AccountRepository;
import ir.dotin.depositservices.bsp.repository.MoneyTransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final MoneyTransferRepository transferRepository;
    private final AccountMapper mapper;

    public AccountService(AccountRepository accountRepository,
                          MoneyTransferRepository transferRepository,
                          AccountMapper mapper) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.mapper = mapper;
    }

    @Transactional
    public AccountResponse create(AccountCreateRequest request) {
        String accountNumber = request.accountNumber().trim();
        if (accountRepository.existsByAccountNumber(accountNumber)) {
            throw new ConflictException("Account number already exists.");
        }
        Account account = mapper.toEntity(request);
        account.setAccountNumber(accountNumber);
        account.setHolderName(request.holderName().trim());
        return mapper.toResponse(accountRepository.save(account));
    }

    @Transactional(readOnly = true)
    public AccountResponse get(Long id) {
        return mapper.toResponse(findAccount(id));
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAll() {
        return accountRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public AccountResponse update(Long id, AccountUpdateRequest request) {
        Account account = findAccount(id);
        account.setHolderName(request.holderName().trim());
        return mapper.toResponse(accountRepository.save(account));
    }

    @Transactional
    public void delete(Long id) {
        Account account = findAccount(id);
        if (transferRepository.existsBySourceAccountIdOrDestinationAccountId(id, id)) {
            throw new ConflictException("Account has transfer history and cannot be deleted.");
        }
        accountRepository.delete(account);
    }

    private Account findAccount(Long id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account " + id + " was not found."));
    }
}
