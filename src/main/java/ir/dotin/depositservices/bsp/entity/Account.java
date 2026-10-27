package ir.dotin.depositservices.bsp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "BSP_ACCOUNT", uniqueConstraints = {
    @UniqueConstraint(name = "UK_BSP_ACCOUNT_NUMBER", columnNames = "ACCOUNT_NUMBER")
})
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bsp_account_seq_gen")
    @SequenceGenerator(name = "bsp_account_seq_gen", sequenceName = "BSP_ACCOUNT_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "ACCOUNT_NUMBER", nullable = false, length = 40)
    private String accountNumber;

    @Column(name = "HOLDER_NAME", nullable = false, length = 120)
    private String holderName;

    @Column(name = "CURRENCY", nullable = false, length = 3)
    private String currency;

    @Column(name = "BALANCE", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public Long getVersion() { return version; }
    public Instant getCreatedAt() { return createdAt; }
}
