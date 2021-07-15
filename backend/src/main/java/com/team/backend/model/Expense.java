package com.team.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //W bazie DATETIME (data utworzenia wydatku)
  /*  @Column(nullable = false)
    private date;*/

    @Column(length = 255)
    @Size(min = 1, max = 255)
    private String receipt_image;

    //Nie wiem czy dobry typ zmiennej
    @Column(nullable = false)
    @NotBlank(message = "Total cost is mandatory!")
    private Double total_cost;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName="id", nullable = false)
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", referencedColumnName="id", nullable = false)
    private Wallet wallet;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName="id", nullable = false)
    private User user;

    @Column(length = 1)
    @Size(min = 1, max = 1)
    private String interval;

}
