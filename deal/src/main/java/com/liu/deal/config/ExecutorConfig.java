package com.liu.deal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ExecutorConfig {
    @Autowired
    private  JdbcTemplate jdbcTemplate;

    @Bean
    public ExecutorService getPool(){
        Object o = jdbcTemplate.queryForMap("select count(1) as count  from market").get("count");
        return Executors.newFixedThreadPool(Integer.valueOf(o.toString())+5);
    }
}
