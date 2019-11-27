package com.poscoict.posledger.assets.service;

import org.hyperledger.fabric.sdk.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisService {

    @Autowired
    SetService s = new SetService();

    @Autowired
    RedisTemplate<String, Object> redisTemplate2 = s.redisTemplate();

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    @Resource(name = "redisTemplate")
    private SetOperations<String, String> valueOps2;


    public void test(String user, Enrollment enrollment) {
        //get/set을 위한 객체
        ValueOperations<String, Object> vop = redisTemplate2.opsForValue();
        //vop.set(user, (String)enrollment);
        //String result = (String) vop.get("jdkSerial");
        //System.out.println(result);//jdk
    }

    public Object get(String user) {
        //get/set을 위한 객체
        ValueOperations<String, Object> vop = redisTemplate2.opsForValue();
        Object o = vop.get(user);
        //String result = (String) vop.get("jdkSerial");
        //System.out.println(result);//jdk

        return o;
    }

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

    public boolean storeUser2(String userID, String certificate) {

        try {
            valueOps.append(userID, certificate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getCertificate(String userID) {

        String certificate = null;

        try {
            certificate = valueOps.get(userID);
            return certificate;
        } catch (Exception e) {
            return null;
        }
    }



}
