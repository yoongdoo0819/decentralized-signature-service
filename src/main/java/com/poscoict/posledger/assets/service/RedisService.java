package com.poscoict.posledger.assets.service;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisService {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    public Long getVisitCount() {
        Long count = 10L;
        try {
            valueOps.increment("spring:redis:visitcount", 1);
            count = Long.valueOf(valueOps.get("spring:redis:visitcount"));
        } catch (Exception e) {
            return 15L;
        }

        return count;
    }
}
