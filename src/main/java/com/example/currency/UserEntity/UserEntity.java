package com.example.currency.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "chat_id",nullable = false, unique = true)
    private String chatId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "admin_state")
    private int adminState;

}
