package com.liu.deal.utils;

import com.liu.deal.model.OrdersData;

import java.util.Comparator;

public class OrdersComparator {

    public final static Comparator<OrdersData> prizeComparatorASC = (o1, o2) -> {
        boolean flag = o1.getId() .equals(o2.getId())  ;
        if (flag) {
            return 0;
        }
        int ret = o1.getPrize().compareTo(o2.getPrize());
        if (ret == 0) {
            return o1.getId().compareTo(o2.getId());
        } else {
            return ret;
        }
    };

    public final static Comparator<OrdersData> prizeComparatorDESC = (o1, o2) -> {
        boolean flag = o1.getId().intValue() == o2.getId().intValue();
        if (flag) {
            return 0;
        }
        int ret = o2.getPrize().compareTo(o1.getPrize());
        if (ret == 0) {
            return o1.getId().compareTo(o2.getId());
        } else {
            return ret;
        }
    };

}
