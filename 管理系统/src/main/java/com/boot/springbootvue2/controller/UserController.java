package com.boot.springbootvue2.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.springbootvue2.common.Result;
import com.boot.springbootvue2.entity.User;
import com.boot.springbootvue2.mapper.UserMapper;
import com.boot.springbootvue2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;
    //注册
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (res != null) {
            return Result.error("-1", "用户名已经存在");
        }
        if(user.getPassword()==null){
            user.setPassword("123456");
        }
        userService.save(user);
        return Result.success(res);
    }

    //登入
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername())
                .eq(User::getPassword, user.getPassword()));
        if (res == null) {
            return Result.error("-1", "用户名或密码错误");
        }
        return Result.success(res);
    }


    //新增
    @PostMapping
    public Result<Object> save(@RequestBody User user){
//        userMapper.insert(user);
        if(user.getPassword()==null){
            user.setPassword("123456");
        }
        userService.save(user);
        return Result.success();
    }

    //更新
    @PutMapping
    public Result<Object> update(@RequestBody User user){
//        userMapper.insert(user);
        if(user.getPassword()==null){
            user.setPassword("123456");
        }
        userService.updateById(user);
        return Result.success();
    }
    //删除

    @DeleteMapping("/{id}")
    public Result<Object> delete(@PathVariable("id") Integer id){
//        userMapper.insert(user);
        userService.removeById(id);
        return Result.success();
    }
    //查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(User::getNickName, search);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(userPage);
    }

    @GetMapping("/{id}")
    public Result<?> getUserById(@PathVariable int id){
        User user = userMapper.selectById(id);
        return Result.success(user);
    }

}
