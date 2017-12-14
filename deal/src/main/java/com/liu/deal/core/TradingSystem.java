package com.liu.deal.core;

import com.liu.deal.model.OrdersData;
import com.liu.deal.utils.EntrustStatusEnum;
import com.liu.deal.utils.EntrustTypeEnum;
import com.liu.deal.utils.OrdersComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 撮合过程
 */
@Service
public class TradingSystem implements Runnable{
    private Logger log = LoggerFactory.getLogger(getClass());
    //这里存放的是所有的挂单信息，其中key是市场id，就是marketId
    private Map<Integer, SortedSet<OrdersData>> buyOrders = new ConcurrentSkipListMap<>();

    private Map<Integer, SortedSet<OrdersData>> sellOrders = new ConcurrentSkipListMap<>();

    //缓存所有挂单信息
    private Map<Integer, OrdersData> ordersMap = new ConcurrentSkipListMap<>();

    //这个阻塞队列是接收rabbitmq的传过来的挂单信息的
    private BlockingDeque<OrdersData> entrustQueue = new LinkedBlockingDeque<>();

    @Autowired
    private DealMaking dealMaking;

    //挂买单，与卖单匹配
    private void matchBuy(OrdersData buy) {
        boolean flag = true;
        Integer id = buy.getMarketId();
        while (flag) {
            SortedSet<OrdersData> sellList = sellOrders.get(id);
            log.debug("matchBuy {}, sell list size = {}", buy.getId(), (sellList == null ? 0 : sellList.size()));
            if (sellList == null || sellList.size() == 0) {
                flag = false;
            } else {
                OrdersData sell = sellList.first();
                Double sellPrize = sell.getPrize();
                Double buyPrize = buy.getPrize();
                if (sellPrize > buyPrize) {
                    log.debug("购买价格小于出售价格，不成立，购买数据中", sellPrize, buyPrize);
                    break;
                }
                // 此时购买的价格比出售的价格高，应该撮合成功。则successCount>0
                double successCount = dealMaking.deal(buy, sell, buy.getMarketId());
                log.debug("updateDealMaking result successCount = {}, buy = {}, leftCount = {}, status = {}, sell = {}, leftCount = {}, status = {}",
                        successCount, buy.getId(), buy.getLeftCount(), buy.getStatus(), sell.getId(), sell.getLeftCount(), sell.getId());
                if (buy.getStatus() == EntrustStatusEnum.AllDeal || buy.getStatus() == EntrustStatusEnum.Cancel) {
                    log.debug("remove buy = {} from map match end.", buy.getId());
                    flag = false;   //挂的是买单，完全成交，执行完下面的就退出
                    ordersMap.remove(buy.getId());
                }
                if (sell.getStatus() == EntrustStatusEnum.AllDeal || sell.getStatus() == EntrustStatusEnum.Cancel) {
                    log.debug("remove sell = {} from map", sell.getId());
                    sellList.remove(sell);
                    ordersMap.remove(sell.getId());
                }
//                boolean success = updateOrders(sell, buy, successCount);
//                if (!success) {
//                    break;
//                }
            }
        }
        addToSortedSet(buyOrders, buy);
    }

    //挂卖单与买单匹配
    private void matchSell(OrdersData sell) {
        boolean flag = true;
        Integer id = sell.getMarketId();
        while (flag) {
            SortedSet<OrdersData> buyList = buyOrders.get(id);
            log.debug("matchSell {}, buy list size = {}", sell.getId(), (buyList == null ? 0 : buyList.size()));
            if (buyList == null || buyList.size() == 0) {
                flag = false;
            } else {
                OrdersData buy = buyList.first();
                Double buyPrize = buy.getPrize();
                Double sellPrize = sell.getPrize();
                if (sellPrize > buyPrize) {
                    log.debug("sell price = {} is greater than buy price = {} match end.", sellPrize, buy.getPrize());
                    break;
                }
                // 成交量大于O，才代表撮合成功
                double successCount = dealMaking.deal(buy, sell, buy.getMarketId());
                log.debug("updateDealMaking result successCount = {}, buy = {}, leftCount = {}, status = {}, sell = {}, leftCount = {}, status = {}", successCount, buy.getId(), buy.getLeftCount(), buy.getStatus(), sell.getId(), sell.getLeftCount(), sell.getStatus());
                if (sell.getStatus() == EntrustStatusEnum.AllDeal || sell.getStatus() == EntrustStatusEnum.Cancel) {
                    log.debug("remove sell = {} from map match end.", sell.getId());
                    flag = false;
                    ordersMap.remove(sell.getId());
                }
                if (buy.getStatus() == EntrustStatusEnum.AllDeal || buy.getStatus() == EntrustStatusEnum.Cancel) {
                    log.debug("remove buy = {} from map", buy.getId());
                    buyList.remove(buy);
                    ordersMap.remove(buy.getId());
                }

//                boolean success = updateOrders(sell, buy, successCount);
//                if (!success) {
//                    break;
//                }
            }
        }
        addToSortedSet(sellOrders, sell);
    }

//    private boolean updateOrders(OrdersData sell, OrdersData buy, double successCount) {
//        //撮合成功一笔，更新深度，返回true，继续撮合
//        if (successCount > 0) {
//            log.debug("updateDepthEntrust(buy = {}, sell = {}, successCount = {})", buy, sell, successCount);
////            updateDepthEntrust(buy, sell, successCount);
//        } else {
//            Integer buyId = buy.getId();
//            Integer sellId = sell.getId();
//            log.debug("match fails sync db to cache buy = {}, sell = {}", buyId, sellId);
//            // 同步数据库状态
//            // 如果匹配失败，则从数据库中重新查询
//            // 错误三次，从挂单从移除，并报警
//            Integer buyErrCount = errorCount.get(buyId);
//            Integer sellErrCount = errorCount.get(sellId);
//            if (buyErrCount != null && buyErrCount == 3) {
//                removeAndSendEmail(entrustBuyMap, buy);
//            } else {
//                errorCount.put(buyId, buyErrCount == null ? 0 : buyErrCount + 1);
//            }
//            if (sellErrCount != null && sellErrCount == 3) {
//                removeAndSendEmail(entrustSellMap, sell);
//            } else {
//                errorCount.put(sellId, sellErrCount == null ? 0 : sellErrCount + 1);
//            }
//
//            if (buyErrCount != null && buyErrCount == 3 || sellErrCount != null && sellErrCount == 3) {
//                return false;
//            }
//
//            OrdersData dbBuy = tradeService.findByFid(buyId);
//            OrdersData dbSell = tradeService.findByFid(sellId);
//            Double buyLeftCount = buy.getFleftCount();
//            Double sellLeftCount = sell.getFleftCount();
//            buyLeftCount = copyData(buy, dbBuy, buyLeftCount);
//            sellLeftCount = copyData(sell, dbSell, sellLeftCount);
//            if (buyLeftCount != buy.getLeftCount()) {
//                log.debug("buy left count not equals db left count id = {}, cache = {}, after = {}", buyId, buyLeftCount, buy.getFleftCount());
//                updateDepthEntrust(buy, null, buyLeftCount);
//            }
//            if (sellLeftCount != sell.getLeftCount()) {
//                log.debug("sell left count not equals db left count id = {}, cache = {}, after = {}", sellId, sellLeftCount, sell.getFleftCount());
//                updateDepthEntrust(null, sell, sellLeftCount);
//            }
//        }
//        return true;
//    }

    //将rabbit监听到的挂单信息加入到阻塞队列和所有的挂单信息中
    private void addOrsersIntoQueue(OrdersData ordersData) {
        OrdersData exists = ordersMap.put(ordersData.getId(), ordersData);
        // 如果还没进到队列，就已经被取消，则不再进队列
        if (exists != null && exists.getStatus() == EntrustStatusEnum.Cancel) {
            ordersMap.remove(ordersData.getId());
            return;
        }
        entrustQueue.add(ordersData);
    }

    private void addToSortedSet(Map<Integer, SortedSet<OrdersData>> map, OrdersData ordersData) {
        // 如果订单没有成交，或者没有取消，就加入到列表里面
        int fstatus = ordersData.getStatus();
        if (fstatus != EntrustStatusEnum.AllDeal && fstatus != EntrustStatusEnum.Cancel) {
            Integer id = ordersData.getMarketId();
            Set<OrdersData> orders = map.get(id);
            //该市场中还没有交易的时候，需要生成一个set存放挂单信息
            if (orders == null) {
                orders = buildSet(map, id, ordersData.getType());
            }
            orders.add(ordersData);
            // 加入到挂单列表的时候，更新价格，在最新的买卖单在最低、最高价格且部分成交时，在将未成交的加到挂单列表，这时需要更新挂单数据
            updateMarking(ordersData.getMarketId(), 0);
        }
    }
    private void updateMarking(int fid, double successPrice) {
        double highestBuy = 0D;
        SortedSet<OrdersData> buyList = buyOrders.get(fid);
        if (buyList != null && buyList.size() > 0) {
            highestBuy = buyList.first().getPrize();
        }
        double lowestSell = 0D;
        SortedSet<OrdersData> sellList = buyOrders.get(fid);
        if (sellList != null && sellList.size() > 0) {
            lowestSell = sellList.first().getPrize();
        }
//        dealMarkingListener.updateMarking(fid, successPrice, highestBuy, lowestSell);
    }

    private SortedSet<OrdersData> buildSet(Map<Integer, SortedSet<OrdersData>> map, int id, int type) {
        synchronized (this){
            SortedSet<OrdersData> ordersData;
            if(type == EntrustTypeEnum.SELL){
                //卖单 价格升序  想知道最小的买家
                ordersData = new ConcurrentSkipListSet<>(OrdersComparator.prizeComparatorASC);
            } else {
                //买单价格降序
                ordersData = new ConcurrentSkipListSet<>(OrdersComparator.prizeComparatorDESC);
            }
            map.put(id, ordersData);
            return ordersData;
        }
    }

    //线程异步调用
    @Override
    public void run() {
        while (flag) {
            try {
                OrdersData orders = entrustQueue.take();
                if (orders.getType() == EntrustTypeEnum.BUY) {
                    matchBuy(orders);
                } else {
                    matchSell(orders);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean flag = true;
    @PreDestroy
    public void destroy(){
        flag = false;
        log.info("应用关闭");
    }
}
