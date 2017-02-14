package com.chas.service.impl;

import com.chas.mapper.UserMapper;
import com.chas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Chas
 * @date 2016-11-07
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
}
