package com.team.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

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
    @Size(min = 1, max = 45, message = "Wielkość nazwy wydatku musi mieć od 1 do 45 znaków!")
    @NotBlank(message = "Nazwa wydatku jest obowiązkowa!")
    private String name;

    @Column(columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime date;

    @Column
    @Size(min = 1, max = 255)
    private String receipt_image;

    @Column(precision = 10, scale = 2, nullable = false)
    @NotNull(message = "Całkowity koszt wydatku jest obowiązkowy!")
    @DecimalMin(value = "0.0", inclusive = false, message = "Całkowity koszt wydatku musi być większy od 0!")
    @Digits(integer = 8, fraction = 2, message = "Całkowty koszt wydatku nie może być większy od 99999999,99!")
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
