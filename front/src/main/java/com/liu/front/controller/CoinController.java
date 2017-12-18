package com.liu.front.controller;

import com.liu.base.dao.CoinDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/12
 */
@RestController
@RequestMapping("/coin")
public class CoinController {

    @Autowired
    private CoinDao coinDao;

    @GetMapping("/all")
    public Object allCoins(){
        return coinDao.findAll();
    }

    @RequestMapping("/hello")
    public String get(){
        return "sghksadg";
    }
}
