package com.team.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
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
    @Size(min = 1, max = 45, message = "{expense.name.size}")
    @NotBlank(message = "{expense.name.notBlank}")
    private String name;

    @Column(columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime date;

    @Column
    @Size(min = 1, max = 255)
    private String receipt_image;

    @Column(precision = 10, scale = 2, nullable = false)
    @NotNull(message = "{expense.totalCost.notNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{expense.totalCost.decimalMin}")
    @Digits(integer = 8, fraction = 2, message = "{expense.totalCost.digits}")
    private BigDecimal total_cost;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName="id", nullable = false)
    private Category category;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id", referencedColumnName="id", nullable = false)
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName="id", nullable = false)
    private User user;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy="expense", cascade = CascadeType.ALL)
    private Set<ExpenseDetail> expenseDetailSet = new HashSet<>();

    public void addExpenseDetail(ExpenseDetail expenseDetail) {
        expenseDetailSet.add(expenseDetail);
        expenseDetail.setExpense(this);
    }
}
