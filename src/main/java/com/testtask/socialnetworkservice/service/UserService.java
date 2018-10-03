package com.testtask.socialnetworkservice.service;

import com.testtask.socialnetworkservice.model.User;

import java.util.List;

public interface UserService {

    List<User> loadUsersByUrl(String url);

    User findById(Long id);
}
