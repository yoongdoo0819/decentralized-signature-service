package com.poscoict.posledger.assets.test;

import com.poscoict.posledger.assets.chaincode.EnrollUser;
import com.poscoict.posledger.assets.config.ExecutionConfig;
import com.poscoict.posledger.assets.config.NetworkConfig;
import com.poscoict.posledger.assets.service.RedisService;
import com.poscoict.posledger.assets.util.RedisEnrollment;
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

        NetworkConfig.ORG0_MSP = "Org0MSP";
        NetworkConfig.ORG0 = "org0";
        NetworkConfig.CA_ORG0_URL = "http://" + IP + ":7054";
        NetworkConfig.ORDERER_URL = "grpc://" + IP + ":7050";
        NetworkConfig.ORG0_PEER_0 = "peer0.org0.example.com";
        NetworkConfig.ORG0_PEER_0_URL = "grpc://" + IP + ":7051";
        NetworkConfig.ORDERER_NAME = "orderer.example.com";
        NetworkConfig.CHANNEL_NAME = "mychannel";
        NetworkConfig.ADMIN = "admin";
        NetworkConfig.ADMIN_PASSWORD = "adminpw";
        NetworkConfig.EVENT_HUB = "grpc://" + IP + ":7053";
        NetworkConfig.CHAINCODE_1_NAME = "mycc";

    }



    @Test
    public void enrollTest() throws Exception {

        EnrollUser enrollToCA = new EnrollUser();

        //  enroll admin
        NetworkConfig.ORG0_MSP = "Org1MSP";
        NetworkConfig.ORG0 = "org1";
        NetworkConfig.CA_ORG0_URL = "http://" + IP + ":8054";
        NetworkConfig.ORG0_PEER_0 = "peer1.org1.example.com";
        NetworkConfig.ORG0_PEER_0_URL = "grpc://" + IP + ":8051";
        NetworkConfig.ADMIN = "admin1";
        enrollToCA.enrollAdmin(NetworkConfig.ADMIN, "adminpw");

        NetworkConfig.ORG0_MSP = "Org2MSP";
        NetworkConfig.ORG0 = "org2";
        NetworkConfig.CA_ORG0_URL = "http://" + IP + ":9054";
        NetworkConfig.ORG0_PEER_0 = "peer2.org2.example.com";
        NetworkConfig.ORG0_PEER_0_URL = "grpc://" + IP + ":9051";
        NetworkConfig.ADMIN = "admin2";
        enrollToCA.enrollAdmin(NetworkConfig.ADMIN, "adminpw");

//
//        //  enroll owner
//        Enrollment enrollment = enrollToCA.registerUser(owner);
//        re.setEnrollment(owner, enrollment);
//
//        //  enroll newOwner
//        enrollment = enrollToCA.registerUser(newOwner);
//        re.setEnrollment(newOwner, enrollment);
//
//        //  enroll approved
//        enrollment = enrollToCA.registerUser(approved);
//        re.setEnrollment(approved, enrollment);
//
//        //  enroll operator
//        enrollment = enrollToCA.registerUser(operator);
//        re.setEnrollment(operator, enrollment);


    }

    @Test
    public void mintTest() throws Exception {
        Enrollment enrollment = re.getEnrollment(owner);
        ExecutionConfig.initUserContext(owner, enrollment);
        //SetConfig.setEnrollment(owner, enrollment);


        //Default de = new Default(new ChaincodeProxy(), "mycc");
        //Boolean result = de.mint("25");
        //assertThat(result).isEqualTo(true);
        //System.out.println(result);
        Thread.sleep(10000);
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
