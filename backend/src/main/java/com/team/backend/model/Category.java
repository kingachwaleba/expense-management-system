package com.team.backend.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Table(name = "category")
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true, nullable = false, length = 45)
    @Size(max = 45)
    private String name;
}
