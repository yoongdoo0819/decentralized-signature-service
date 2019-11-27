package com.poscoict.posledger.assets.service;

import org.hyperledger.fabric.sdk.Enrollment;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisService {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    @Resource(name = "redisTemplate")
    private SetOperations<String, String> valueOps2;


    public Long getVisitCount() {
        Long count = 10L;
        try {
            valueOps.increment("spring:redis:visitcount", 1);
            //count = Long.valueOf(valueOps.get("spring:redis:visitcount"));
        } catch (Exception e) {
            return 15L;
        }

        return count;
    }

    public boolean storeUser(String userID, Enrollment certificate) {

        try {
            //valueOps.setIfAbsent(userID, certificate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean setUserInfo(String userID, String certificate) {

        try {
            valueOps.append(userID, certificate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserInfo(String userID) {

        String enrollment = null;

        try {
            enrollment = valueOps.get(userID);
            return enrollment;
        } catch (Exception e) {
            return null;
        }
    }



}
