package com.testtask.socialnetworkservice.service.impl;

import com.testtask.socialnetworkservice.exception.ResourceNotFoundException;
import com.testtask.socialnetworkservice.model.User;
import com.testtask.socialnetworkservice.repository.UserRepository;
import com.testtask.socialnetworkservice.service.ExternalDataLoadService;
import com.testtask.socialnetworkservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final ExternalDataLoadService<User> loadService;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(ExternalDataLoadService<User> loadService, UserRepository userRepository) {
        this.loadService = loadService;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public List<User> loadUsersByUrl(String url) {
        List<User> users = loadService.loadFromUrl(url, User[].class);
        return userRepository.saveAll(users);
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User by id " + id + " not found!"));
    }
}
