package com.tanlw.service;

import com.tanlw.web.User;

import java.util.List;

public interface UserService {

    User find(Long id);

    List<User> findAll(List<Long> ids);
}
