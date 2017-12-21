package com.liu.base.service;

import com.liu.base.dao.WalletDao;
import com.liu.base.entity.Coin;
import com.liu.base.entity.User;
import com.liu.base.entity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/19
 */
@Service
public class WalletService {
    @Autowired
    WalletDao walletDao ;

    public Wallet findByUserAndCoin(User user, Coin coin){
        return walletDao.findByCoinAndUser(coin, user);
    }
}
