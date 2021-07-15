package com.team.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver", referencedColumnName="id")
    private User receiver;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", referencedColumnName="id", nullable = false)
    private Wallet wallet;

    //W bazie DATETIME (data wysłania wiadomości, generowana przez system)
  /*  @Column(nullable = false)
    private date;*/

    @Column(nullable = false)
    @Size(min = 1, max = 255)
    @NotBlank(message = "Content is mandatory!")
    private String content;

    //Typ wiadomości (wiadomość czy powiadomienie) generowane przez system
    @Column(nullable = false, length = 1)
    @Size(min = 1, max = 1)
    private String type;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sender", referencedColumnName="id")
    private User sender;

}
