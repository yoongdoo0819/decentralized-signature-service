package com.github.fabasset.example.test;

import com.github.fabasset.example.chaincode.EnrollUser;
import com.github.fabasset.example.config.NetworkConfig;
import com.github.fabasset.example.service.RedisService;
import com.github.fabasset.example.util.RedisEnrollment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class EnrollTest {

    private static final Logger logger = LoggerFactory.getLogger(EnrollTest.class);

    @Autowired
    RedisEnrollment re;

    @Autowired
    RedisService redisService;
    final String IP = "localhost";

    public EnrollTest() throws Exception{

        NetworkConfig.CHANNEL_NAME = "mychannel";
        NetworkConfig.EVENT_HUB = "grpc://" + IP + ":7053";
        NetworkConfig.CHAINCODE_NAME = "mycc";
        NetworkConfig.ORDERER_URL = "grpc://localhost:7050";
        NetworkConfig.ORDERER_NAME = "orderer.example.com";
    }

    @Test
    public void enrollTest() throws Exception {

        EnrollUser enrollToCA = new EnrollUser();

        // enroll admin1
        NetworkConfig.ORG_MSP = "Org1MSP";
        NetworkConfig.ORG = "org1";
        NetworkConfig.CA_ORG_URL = "http://" + IP + ":8054";
        NetworkConfig.ORG_PEER = "peer1.org1.example.com";
        NetworkConfig.ORG_PEER_URL = "grpc://" + IP + ":8051";
        NetworkConfig.ADMIN = "admin1";
        enrollToCA.enrollAdmin(NetworkConfig.ADMIN, "adminpw");

        // enroll admin2
        NetworkConfig.ORG_MSP = "Org2MSP";
        NetworkConfig.ORG = "org2";
        NetworkConfig.CA_ORG_URL = "http://" + IP + ":9054";
        NetworkConfig.ORG_PEER = "peer2.org2.example.com";
        NetworkConfig.ORG_PEER_URL = "grpc://" + IP + ":9051";
        NetworkConfig.ADMIN = "admin2";
        enrollToCA.enrollAdmin(NetworkConfig.ADMIN, "adminpw");

    }

}
