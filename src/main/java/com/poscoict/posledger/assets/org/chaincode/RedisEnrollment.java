package com.poscoict.posledger.assets.org.chaincode;

import com.poscoict.posledger.assets.service.RedisService;
import org.hyperledger.fabric.sdk.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

@Service
public class RedisEnrollment {

    @Autowired
    RedisService redisService;// = new RedisService();

    public boolean setEnrollment(String owner, Enrollment enrollment)  throws Exception {
        byte[] serializedMember;

        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(enrollment);
                // serialized enrollment
                serializedMember = baos.toByteArray();
                redisService.setUserInfo(owner, Base64.getEncoder().encodeToString(serializedMember));

                return true;
            }
        }
    }

    public Enrollment getEnrollment(String owner) throws Exception {

        String enrollmentSTring = redisService.getUserInfo(owner);
        Enrollment enrollment;
        byte[] serializedMember = Base64.getDecoder().decode(enrollmentSTring);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedMember)) {
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                //deserialize
                Object objectMEnrollment = ois.readObject();
                enrollment = (Enrollment) objectMEnrollment;
                System.out.println(enrollment);

                return enrollment;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
