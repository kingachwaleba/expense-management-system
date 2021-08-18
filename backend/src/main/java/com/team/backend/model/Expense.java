package com.team.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, length = 45)
    @Size(min = 1, max = 45)
    @NotBlank(message = "Expense name is mandatory!")
    private String name;

    @Column(columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime date;

    @Column(length = 255)
    @Size(min = 1, max = 255)
    private String receipt_image;

    @Column(precision = 10, scale = 2, nullable = false)
    @NotNull(message = "Total cost is mandatory!")
    private BigDecimal total_cost;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName="id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id", referencedColumnName="id", nullable = false)
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName="id", nullable = false)
    private User user;

    @Column(length = 1)
    @Size(min = 1, max = 1)
    private String period;

    @OneToMany(mappedBy="expense", cascade = CascadeType.ALL)
    private Set<ExpenseDetail> expenseDetailSet = new HashSet<>();

    public void addExpenseDetail(ExpenseDetail expenseDetail) {
        expenseDetailSet.add(expenseDetail);
        expenseDetail.setExpense(this);
    }
}
