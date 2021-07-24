package com.team.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(columnDefinition = "DATETIME", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(length = 255)
    @Size(min = 1, max = 255)
    private String receipt_image;

    @Column(precision = 10, scale = 2, nullable = false)
    @NotBlank(message = "Total cost is mandatory!")
    private BigDecimal total_cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName="id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", referencedColumnName="id", nullable = false)
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName="id", nullable = false)
    private User user;

    @Column(length = 1)
    @Size(min = 1, max = 1)
    private String period;
}
