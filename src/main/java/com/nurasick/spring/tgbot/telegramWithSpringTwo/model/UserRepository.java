package com.nurasick.spring.tgbot.telegramWithSpringTwo.model;

import com.nurasick.spring.tgbot.telegramWithSpringTwo.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
