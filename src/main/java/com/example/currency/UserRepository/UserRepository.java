package com.example.currency.UserRepository;

import com.example.currency.UserEntity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    Optional<UserEntity> findByChatId(String chatId);

    @Override
    List<UserEntity> findAll();
}
