package com.liu.front.controller;

import com.liu.base.dao.PhoneMessageDao;
import com.liu.base.dao.UserDao;
import com.liu.base.entity.PhoneMessage;
import com.liu.base.entity.User;
import com.liu.base.utils.RandomUtils;
import com.liu.front.utils.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/13
 */
@RestController
public class LoginController extends BaseController{
    @Autowired
    UserDao userDao;

    @Autowired
    PhoneMessageDao phoneMessageDao;

    @PostMapping("/login")
    public Object login(String phone, String password, String code, HttpSession session){
        if("".equals(phone)){
            return result(1,"手机号不能为空",null);
        }
        if("".equals(password)){
            return result(2,"密码不能为空",null);
        }
        if("".equals(code)){
            return result(3,"验证码不能为空",null);
        }
        String realCode = (String) session.getAttribute(Keys.phone_code);
        if(!code.equals(realCode)){
            return result(4,"验证码不正确",null);
        }
        User user = userDao.findByPhoneAndPassword(phone,password);
        if(user == null){
            return result(5,"用户名或密码错误",null);
        }
        session.setAttribute(Keys.login_user, user);
        return result(200,"ok",null);
    }

    @GetMapping("/sendCode")
    public Object sendCode(HttpSession session, String phone){
        String code = RandomUtils.randomInteger(6);
        PhoneMessage message = new PhoneMessage();
        message.setMessage(code);
        message.setStatus(1);
        message.setCreateTime(new Date());
        message.setPhone(phone);
        phoneMessageDao.save(message);
        session.setAttribute(Keys.phone_code,code);
        return result(200,"ok",null);
    }
}
