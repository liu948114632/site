package com.liu.front.service;

import com.liu.base.dao.InAddressDao;
import com.liu.base.dao.OutAddressDao;
import com.liu.base.dao.VirtualOperationDao;
import com.liu.base.dao.WalletDao;
import com.liu.base.entity.*;
import com.liu.base.utils.MathUtils;
import com.liu.base.utils.VirtualOperationOutStatusEnum;
import com.liu.base.utils.VirtualOperationTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
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
        return virtualOperationDao.findByUserAndCoinAndType(user ,coin ,type);
    }
    public void saveOutAddress(OutAddress address){
        outAddressDao.save(address);
    }

    public OutAddress getOutAddressByAddress(String address){
        return outAddressDao.getOutAddressByAdderess(address);
    }

    public void addOutOperate(String address, Coin coin, User user, double amount) {
        double rate = coin.getWithdrawRate();
        double realAmount = MathUtils.multiply(amount, (1-rate));
        VirtualOperation virtualOperation = new VirtualOperation();
        virtualOperation.setAmount(realAmount);
        virtualOperation.setCoin(coin);
        virtualOperation.setUser(user);
        virtualOperation.setCreateTime(new Timestamp((new Date()).getTime()));
        virtualOperation.setStatus(VirtualOperationOutStatusEnum.WaitForOperation);
        virtualOperation.setToAddress(address);
        virtualOperation.setFees(MathUtils.multiply(amount ,rate));
        virtualOperation.setType(VirtualOperationTypeEnum.COIN_OUT);
        virtualOperationDao.save(virtualOperation);
    }
}
