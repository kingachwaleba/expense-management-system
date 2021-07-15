package com.team.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "wallet_user")
public class WalletUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName="id", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_status_id", referencedColumnName="id", nullable = false)
    private UserStatus userStatus;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", referencedColumnName="id", nullable = false)
    private Wallet wallet;

    @Column(columnDefinition = "DATETIME", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date created_at;

    @Column(columnDefinition = "DATETIME")
    @Temporal(TemporalType.DATE)
    private Date accepted_at;
}
