package com.liu.base.dao;

import com.liu.base.entity.OutAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutAddressDao extends JpaRepository<OutAddress, Integer> {
    OutAddress getOutAddressByAdderess(String address);

}
