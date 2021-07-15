package com.team.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Entity
@Table(name = "list_detail")
public class ListDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, length = 100)
    @Size(min = 1, max = 100)
    @NotBlank(message = "Name of product is mandatory!")
    private String name;

    @Column(nullable = false, length = 45)
    @Size(min = 1, max = 45)
    @NotBlank(message = "Quantity is mandatory!")
    private String quantity;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", referencedColumnName="id", nullable = false)
    private List list;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", referencedColumnName="id", nullable = false)
    private Unit unit;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", referencedColumnName="id", nullable = false)
    private Status status;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName="id")
    private User user;
}
