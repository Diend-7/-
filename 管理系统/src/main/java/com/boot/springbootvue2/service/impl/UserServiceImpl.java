package com.boot.springbootvue2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.springbootvue2.entity.User;
import com.boot.springbootvue2.mapper.UserMapper;
import com.boot.springbootvue2.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
