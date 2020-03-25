package com.poscoict.posledger.assets.test;

import com.poscoict.posledger.assets.chaincode.EnrollmentUser;
import com.poscoict.posledger.assets.chaincode.RedisEnrollment;
import com.poscoict.posledger.assets.config.SetConfig;
import com.poscoict.posledger.assets.chaincode.standard.Default;
import com.poscoict.posledger.assets.service.RedisService;
import com.poscoict.posledger.assets.util.Manager;
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

//import com.poscoict.posledger.assets.org.chaincode.UserConfig;

//import com.poscoict.posledger.assets.org.chaincode.ERC721.ERC721;
//import com.poscoict.posledger.assets.org.chaincode.EnrollmentUser;
//import com.poscoict.posledger.assets.org.chaincode.UserConfig;
//import com.poscoict.posledger.assets.config.Config;
//import com.poscoict.posledger.assets.user.UserContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class ERC721Test {

    //@Autowired
    //private ERC721 erc721 = new ERC721();

    private static final Logger logger = LoggerFactory.getLogger(ERC721Test.class);
    String owner = "alice";
    String newOwner = "bob";
    String approved = "carol";
    String operator = "david";
    String tokenId = "0";

    @Autowired
    RedisEnrollment re;

    @Autowired
    RedisService redisService;

    //@Autowired
    //UserConfig UserConfig;

    final String IP = "localhost";
    public ERC721Test() throws Exception{
        /*
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
        Manager.setChaincodeId("mycc");

         */
    }



    @Test
    public void enrollTest() throws Exception {

        EnrollmentUser enrollToCA = new EnrollmentUser();

        //  enroll admin
        //enrollToCA.enrollAdmin();

        /*
        //  enroll owner
        Enrollment enrollment = enrollToCA.registerUser(owner);
        re.setEnrollment(owner, enrollment);

        //  enroll newOwner
        enrollment = enrollToCA.registerUser(newOwner);
        re.setEnrollment(newOwner, enrollment);

        //  enroll approved
        enrollment = enrollToCA.registerUser(approved);
        re.setEnrollment(approved, enrollment);

        //  enroll operator
        enrollment = enrollToCA.registerUser(operator);
        re.setEnrollment(operator, enrollment);

         */
    }

    @Test
    public void mintTest() throws Exception {
        Manager.setChaincodeId("mycc");
        Enrollment enrollment = re.getEnrollment(owner);
        SetConfig.initUserContext(owner, enrollment);
        //SetConfig.setEnrollment(owner, enrollment);


        Default de = new Default();
        Boolean result = de.mint("6");
        assertThat(result).isEqualTo(true);
    }
/*
    @Test
    public void registerTest() throws Exception {

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(owner);
        UserConfig.setEnrollment(owner, enrollment);


        String result = erc721.register(tokenId, owner);
        assertThat(result).isEqualTo("SUCCESS");
    }

    @Test
    public void balanceOfTest() throws Exception {

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(owner);
        UserConfig.setEnrollment(owner, enrollment);

        assertThat(erc721.balanceOf(owner)).isEqualTo("1");
    }

    @Test
    public void ownerOfTest() throws Exception {

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(owner);
        UserConfig.setEnrollment(owner, enrollment);

        UserContext userContext = UserConfig.initUserContextForOwner();
        X509Identity identity = new X509Identity(userContext);
        String addr = AddressUtils.getMyAddress(identity);

        assertThat(erc721.ownerOf(tokenId)).isEqualTo(addr);
    }

    @Test
    public void transferFromTest() throws Exception {

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(owner);
        UserConfig.setEnrollment(owner, enrollment);

        Enrollment enrollmentForNewOwner = re.getEnrollment(newOwner);
        UserConfig.setEnrollmentForNewOwner(newOwner, enrollmentForNewOwner);

        assertThat(erc721.transfer(owner, newOwner, tokenId)).isEqualTo("SUCCESS");
    }

    @Test
    public void afterThatBalanceOfTest() throws Exception {

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(owner);
        UserConfig.setEnrollment(owner, enrollment);

        assertThat(erc721.balanceOf(owner)).isEqualTo("0");
    }

    @Test
    public void afterThatOwnerOfTest() throws Exception {

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(newOwner);
        UserConfig.setEnrollment(newOwner, enrollment);

        UserContext userContext = UserConfig.initUserContextForOwner();
        X509Identity identity = new X509Identity(userContext);
        String addr = AddressUtils.getMyAddress(identity);

        assertThat(erc721.ownerOf(tokenId)).isEqualTo(addr);
    }

    @Test
    public void approveTest() throws Exception {

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(newOwner);
        UserConfig.setEnrollment(newOwner, enrollment);

        Enrollment enrollmentForApproved = re.getEnrollment(approved);
        UserConfig.setEnrollmentForApproved(approved, enrollmentForApproved);

        assertThat(erc721.approve(approved, tokenId)).isEqualTo("SUCCESS");
    }

    @Test
    public void getApprovedTest() throws Exception {

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(owner);
        UserConfig.setEnrollment(owner, enrollment);

        Enrollment enrollmentForApproved = re.getEnrollment(approved);
        UserConfig.setEnrollmentForApproved(approved, enrollmentForApproved);

        UserContext userContext = UserConfig.initUserContextForApproved();
        X509Identity identity = new X509Identity(userContext);
        String addr = AddressUtils.getMyAddress(identity);

        assertThat(erc721.getApproved(tokenId)).isEqualTo(addr);
    }

    @Test
    public void setApprovalForAllTest() throws Exception {

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(newOwner);
        UserConfig.setEnrollment(newOwner, enrollment);

        enrollment = re.getEnrollment(operator);
        UserConfig.setEnrollmentForOperator(operator, enrollment);

        assertThat(erc721.setApprovalForAll(newOwner, operator, "true")).isEqualTo("SUCCESS");
    }

    @Test
    public void isApprovedForAllTest() throws Exception {

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(newOwner);
        UserConfig.setEnrollment(newOwner, enrollment);

        enrollment = re.getEnrollment(operator);
        UserConfig.setEnrollmentForOperator(operator, enrollment);

        assertThat(erc721.isApprovedForAll(newOwner, operator)).isEqualTo("TRUE");
    }

 */

}
