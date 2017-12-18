package com.liu.front.service;

import com.liu.base.dao.InAddressDao;
import com.liu.base.dao.OutAddressDao;
import com.liu.base.dao.VirtualOperationDao;
import com.liu.base.dao.WalletDao;
import com.liu.base.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {
    @Autowired
    WalletDao walletDao;

    @Autowired
    InAddressDao inAddressDao;

    @Autowired
    VirtualOperationDao virtualOperationDao;

    @Autowired
    OutAddressDao outAddressDao;

    public List<Wallet> getAccount(User user){
        return walletDao.findByUser(user);
    }

    public String getInAddress(int userId, int coinId){
        InAddress address =  inAddressDao.findInAddressByUserIdAndCoinId(userId, coinId);
        if(address !=null){
            return address.getAddress();
        }
        //从地址池中获取，地址池不存在则调用钱包
        return null;
    }

    public List<VirtualOperation> getVirtualOperation(User user, Coin coin, int type){
        return virtualOperationDao.findByUserAndCoinaAndType(user ,coin ,type);
    }
    public void saveOutAddress(OutAddress address){
        outAddressDao.save(address);
    }

    public OutAddress getOutAddressByAddress(String address){
        return outAddressDao.getOutAddressByAdderess(address);
    }
}
