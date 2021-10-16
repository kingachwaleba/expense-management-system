package com.team.backend.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "list")
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, length = 45)
    @Size(min = 1, max = 45, message = "{shoppingList.name.size}")
    @NotBlank(message = "{shoppingList.name.notBlank}")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id", referencedColumnName="id", nullable = false)
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", referencedColumnName="id", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName="id")
    private User user;

    @OneToMany(mappedBy="list", cascade = CascadeType.ALL)
    private Set<ListDetail> listDetailSet = new HashSet<>();

    public void addListDetail(ListDetail listDetail) {
        listDetailSet.add(listDetail);
        listDetail.setList(this);
    }
}
