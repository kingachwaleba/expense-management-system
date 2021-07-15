package com.team.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 45)
    @Size(min = 1, max = 45)
    @NotBlank(message = "Wallet name is mandatory!")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_category_id", referencedColumnName="id", nullable = false)
    private WalletCategory walletCategory;

    @Column(length = 1000)
    @Size(max = 1000)
    private String description;
}
