package com.liu.front.controller;

import com.liu.base.entity.User;
import com.liu.front.utils.Keys;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/13
 */
public class BaseController {
    public  User getUser(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        return (User) session.getAttribute(Keys.login_user);
    }

    public Map result(int code, String msg, Object data){
        Map result = new HashMap();
        result.put("code", code);
        result.put("msg", msg);
        result.put("data", data);
        return result;
    }
}
