package com.liu.front.controller;

import com.liu.base.entity.*;
import com.liu.base.service.WalletService;
import com.liu.base.utils.VirtualOperationTypeEnum;
import com.liu.front.service.AssetService;
import com.liu.front.service.MarketAndCoinCache;
import com.liu.front.utils.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/asset")
public class AssetController extends BaseController{
    @Autowired
    AssetService assetService;

    @Autowired
    MarketAndCoinCache marketAndCoinCache;

    @Autowired
    WalletService walletService;

    @RequestMapping("/account")
    public Object getAccount(){
        List<Wallet> list = assetService.getAccount(getUser());
        return result(200,"ok",list);
    }

    /**
     * 充值地址
     * @param coinId
     * @return
     */
    @RequestMapping("/inAddress")
    public Object getInAddress(int coinId){
        User user = getUser();
        List<Coin> coins = marketAndCoinCache.getCoins();
        for (Coin coin : coins){
            if (!coin.isRecharge()){
                coins.remove(coin);
            }
        }
        Coin coin = marketAndCoinCache.getCoinById(coinId);

        if(coin == null){
            return result(1, "没有该币种", coins);
        }
        if(!coin.isRecharge()){
            return result(1, "禁止充值", coins);
        }

        String address = assetService.getInAddress(user.getId(), coinId);

        List<VirtualOperation> operations = assetService.getVirtualOperation(user, coin, VirtualOperationTypeEnum.COIN_IN);

        Map<String ,Object> map = new HashMap<>();
        map.put("coins", coins);
        map.put("address", address);
        map.put("operates", operations);

        return result(200, "ok", map);
    }

    /**
     * 提现记录
     * @param coinId
     * @return
     */
    @RequestMapping("/getOutOperate")
    public Object getOutOperate(int coinId){
        User user = getUser();
        List<Coin> coins = marketAndCoinCache.getCoins();
        for (Coin coin : coins){
            if (!coin.isWithDraw()){
                coins.remove(coin);
            }
        }
        Coin coin = marketAndCoinCache.getCoinById(coinId);

        if(coin == null){
            return result(1, "没有该币种", coins);
        }

        List<VirtualOperation> operations = assetService.getVirtualOperation(user, coin, VirtualOperationTypeEnum.COIN_OUT);
        return result(200, "ok", operations);
    }


    /**
     * 新增提现地址
     *
     * @param session
     * @param coinId
     * @param code  验证码
     * @return
     */
    @RequestMapping("/addOutAddress")
    public Object addOutAddress(HttpSession session , int coinId, String code ,
                                String address, @RequestParam(required = false) String label, int isDefault){
        String realCode = (String)session.getAttribute(Keys.phone_code);

        if (realCode == null){
            return result(1,"未发送验证码！",null);
        }
        if(code == null){
            return result(2,"验证码不能为空！" ,null);
        }
        if (!realCode.equals(code)){
            return result(3,"验证码不正确！" ,null);
        }
        OutAddress outAddress = new OutAddress();
        outAddress.setCoinId(coinId);
        outAddress.setAdderess(address);
        outAddress.setLabel(label);
        outAddress.setDefault(isDefault == 1);
        outAddress.setUserId(getUser().getId());
        assetService.saveOutAddress(outAddress);
        return result(200,"ok",null);
    }

    /**
     * 申请提现
     * @return
     */
    @RequestMapping("/applyWithdraw")
    public Object applyWithdraw(String address, int coinId, String code, HttpSession session, double amount){
        String realCode = (String)session.getAttribute(Keys.phone_code);

        if (realCode == null){
            return result(1,"未发送验证码！",null);
        }
        if(code == null){
            return result(2,"验证码不能为空！" ,null);
        }
        if (!realCode.equals(code)){
            return result(3,"验证码不正确！" ,null);
        }
        OutAddress outAddress = assetService.getOutAddressByAddress(address);

        if(outAddress == null){
            return result(4,"提现地址不正确！" ,null);
        }
        Coin coin = marketAndCoinCache.getCoinById(coinId);
        if(coin == null){
            return result(5, "没有该币种", null);
        }
        if(!coin.isWithDraw()){
            return result(6, "禁止提现", null);
        }

        //申请提现
        Wallet wallet = walletService.findByUserAndCoin(getUser(), coin);
        if(wallet.getTotal() < amount){
            return result(7, "余额不足", null);
        }

        assetService.addOutOperate(address, coin, getUser(),amount);

        return result(200, "申请提现成功", null);
    }
}
