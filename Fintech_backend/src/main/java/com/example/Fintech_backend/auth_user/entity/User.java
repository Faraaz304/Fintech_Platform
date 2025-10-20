package com.example.Fintech_backend.auth_user.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.Fintech_backend.account.entity.Account;
import com.example.Fintech_backend.enums.AccountStatus;
import com.example.Fintech_backend.enums.AccountType;
import com.example.Fintech_backend.enums.Currency;
import com.example.Fintech_backend.role.entity.Role;
import com.example.Fintech_backend.transaction.entity.Transaction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
@Builder
public class User {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private Long id;

    // @Column(nullable = false, unique = true, length = 15)
    // private String accountNumber;

    // @Column(nullable = false, precision = 19, scale = 2)
    // private BigDecimal balance = BigDecimal.ZERO;

    // @Enumerated(EnumType.STRING)
    // @Column(nullable = false)
    // private AccountType accountType;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user;

    // @Enumerated(EnumType.STRING)
    // private Currency currency;

    // @Enumerated(EnumType.STRING)
    // private AccountStatus status;

    // @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // private List<Transaction> transactions = new ArrayList<>();

    // private LocalDateTime closedAt;

    // private LocalDateTime createdAt = LocalDateTime.now();
    // private LocalDateTime updatedAt;


    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;
    private String phoneNumber;

    @Email
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    private String email;

    private String password;
    private String profilePictureUrl;
    private boolean active = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accounts;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;


}
