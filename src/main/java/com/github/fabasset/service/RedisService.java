package com.github.fabasset.service;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisService {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

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
