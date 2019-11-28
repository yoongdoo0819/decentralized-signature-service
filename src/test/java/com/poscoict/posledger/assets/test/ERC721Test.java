package com.poscoict.posledger.assets.test;

import com.poscoict.posledger.assets.org.chaincode.ERC721.ERC721;
import com.poscoict.posledger.assets.org.chaincode.EnrollmentUser;
import com.poscoict.posledger.assets.org.chaincode.RedisEnrollment;
import com.poscoict.posledger.assets.org.chaincode.SetConfig;
import com.poscoict.posledger.assets.org.config.Config;
import com.poscoict.posledger.assets.service.RedisService;
import org.hyperledger.fabric.sdk.Enrollment;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class ERC721Test {

    @Autowired
    private ERC721 erc721;

    private static final Logger logger = LoggerFactory.getLogger(ERC721Test.class);
    String owner = "yazqqqq";
    String newOwner = "zaqqqq";
    String approved = "carol";
    String operator = "david";
    String tokenId = "0";

    @Autowired
    RedisEnrollment re;// = new RedisEnrollment();

    @Autowired
    RedisService redisService;

    @Autowired
    SetConfig setConfig;

    final String IP = "localhost";
    public ERC721Test() throws Exception{
        Config.ORG1_MSP = "Org1MSP";
        Config.ORG1 = "org1";
        Config.ADMIN = "admin";
        Config.ADMIN_PASSWORD = "adminpw";
        Config.CA_ORG1_URL = "http://" + IP + ":7054";
        Config.ORDERER_URL = "grpc://" + IP + ":7050";
        Config.ORDERER_NAME = "orderer.example.com";
        Config.CHANNEL_NAME = "mychannel";
        Config.ORG1_PEER_0 = "peer0.org1.example.com";
        Config.ORG1_PEER_0_URL = "grpc://" + IP + ":7051";
        Config.EVENT_HUB = "grpc://" + IP + ":7053";
        Config.CHAINCODE_1_NAME = "mycc";

    }

    @Test
    public void enrollTest() throws Exception {

        EnrollmentUser enrollToCA = new EnrollmentUser();

        //  enroll admin
        enrollToCA.enrollAdmin();

        //  enroll owner
        Enrollment enrollment = enrollToCA.registerUser(owner);
        re.setEnrollment(owner, enrollment);

        //  enroll newOwner
        enrollment = enrollToCA.registerUser(newOwner);
        re.setEnrollment(newOwner, enrollment);
    }

    @Test
    public void registerTest() throws Exception {

        setConfig.initUserContext(owner);
        String result = erc721.register(tokenId, owner);
        assertThat(result).isEqualTo("SUCCESS");
    }

    @Test
    public void balanceOfTest() throws Exception {

        setConfig.initUserContext(newOwner);
        assertThat(erc721.balanceOf(newOwner)).isEqualTo("0");
    }

    @Test
    public void ownerOfTest() throws Exception {

        assertThat(erc721.ownerOf(tokenId, owner)).isEqualTo(owner);
    }

    @Test
    public void transferFromTest() throws Exception {

        assertThat(erc721.transfer(owner, newOwner, tokenId)).isEqualTo("SUCCESS");
    }

    @Test
    public void afterThatBalanceOfTest() throws Exception {

        assertThat(erc721.balanceOf(owner)).isEqualTo("0");
    }

    @Test
    public void afterThatOwnerOfTest() throws Exception {

        assertThat(erc721.ownerOf(tokenId, newOwner)).isEqualTo(newOwner);
    }

    @Test
    public void approveTest() throws Exception {

        assertThat(erc721.approve(approved, tokenId, newOwner)).isEqualTo("SUCCESS");
    }

    @Test
    public void getApprovedTest() throws Exception {

        assertThat(erc721.getApproved(tokenId, newOwner)).isEqualTo(approved);
    }

    @Test
    public void setApprovalForAllTest() throws Exception {

        assertThat(erc721.setApprovalForAll(newOwner, operator, "true")).isEqualTo("SUCCESS");
    }

    @Test
    public void isApprovedForAllTest() throws Exception {

        assertThat(erc721.isApprovedForAll(newOwner, operator)).isEqualTo("TRUE");
    }

}
