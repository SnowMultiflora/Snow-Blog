package com.zxw.service;

import com.zxw.pojo.User;

public interface UserService {
    //登录
    User checkUser(String username,String password);
}
