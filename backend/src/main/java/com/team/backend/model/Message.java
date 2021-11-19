package com.team.backend.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver", referencedColumnName="id")
    private User receiver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id", referencedColumnName="id", nullable = false)
    private Wallet wallet;

    @Column(columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    @Size(min = 1, max = 255)
    @NotBlank(message = "Content is mandatory!")
    private String content;

    @Column(nullable = false, length = 1)
    @Size(min = 1, max = 1)
    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender", referencedColumnName="id")
    private User sender;

    public Message(User receiver, Wallet wallet, LocalDateTime date, String content, String type, User sender) {
        this.receiver = receiver;
        this.wallet = wallet;
        this.date = date;
        this.content = content;
        this.type = type;
        this.sender = sender;
    }

    public enum MessageType {
        S, // notification about debts sent by system
        E, // notification about debts sent by user
        M, // message sent by user
        R, // notification about messages sent by system
    }
}
