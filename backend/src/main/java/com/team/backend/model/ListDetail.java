package com.team.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "list_detail")
public class ListDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, length = 100)
    @Size(min = 1, max = 100, message = "{listDetail.name.size}")
    @NotBlank(message = "{listDetail.name.notBlank}")
    private String name;

    @Column(precision = 8, scale = 3, nullable = false)
    @NotNull(message = "{listDetail.quantity.notNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{listDetail.quantity.decimalMin}")
    @Digits(integer = 5, fraction = 3, message = "{listDetail.quantity.digits}")
    private BigDecimal quantity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "list_id", referencedColumnName="id", nullable = false)
    private ShoppingList list;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unit_id", referencedColumnName="id", nullable = false)
    private Unit unit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", referencedColumnName="id", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName="id")
    private User user;
}
