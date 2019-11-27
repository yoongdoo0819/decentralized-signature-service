package com.poscoict.posledger.assets.test;

import com.poscoict.posledger.assets.org.chaincode.ERC721.ERC721;
import com.poscoict.posledger.assets.org.chaincode.EnrollmentUser;
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

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

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
    String owner = "yabcd";
    String newOwner = "zabcd";
    String approved = "carol";
    String operator = "david";
    String tokenId = "0";

    @Autowired
    RedisService redisService;

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


/*
        Util.cleanUp();
        String caUrl = Config.CA_ORG1_URL;
        java.util.logging.Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO, " ####################################################### " + caUrl);
        CAClient caClient = null;
        try {
            caClient = new CAClient(caUrl, null);
        } catch (MalformedURLException e) {

        }
        UserContext adminUserContext = new UserContext();
        adminUserContext.setName(owner);
        adminUserContext.setAffiliation(Config.ORG1);
        adminUserContext.setMspId(Config.ORG1_MSP);
        caClient.setAdminUserContext(adminUserContext);
        adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

        UserContext userContext = new UserContext();
        String name = owner;
        userContext.setName(name);
        userContext.setAffiliation(Config.ORG1);
        userContext.setMspId(Config.ORG1_MSP);

 */
/*
        RedisService redisService = new RedisService();
        String certificate = redisService.getCertificate(owner);
        userContext = caClient.enrollUser(userContext, certificate);

        FabricClient fabClient = new FabricClient(userContext);

        ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
        Channel channel = channelClient.getChannel();
        Peer peer = fabClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
        EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", Config.EVENT_HUB);
        Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
        channel.addPeer(peer);
        channel.addEventHub(eventHub);
        channel.addOrderer(orderer);
        channel.initialize();

 */
    }

    @Test
    public void enrollTest() throws Exception {

        EnrollmentUser enrollToCA = new EnrollmentUser();

        enrollToCA.enrollAdmin();
        Enrollment certificate = enrollToCA.registerUser(owner);

        //redisService.storeUser(owner, certificate);
        byte[] serializedMember;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(certificate);
                // serializedMember -> 직렬화된 member 객체
                serializedMember = baos.toByteArray();
            }
        }
        redisService.storeUser2(owner, Base64.getEncoder().encodeToString(serializedMember));
        //redisService.test(owner, certificate);
        System.out.println("+++++++++++++++++++ " + certificate.getCert());

        certificate = enrollToCA.registerUser(newOwner);
        redisService.storeUser(newOwner, certificate);
    }

    @Test
    public void registerTest() throws Exception {

        String result = erc721.register(tokenId, owner);
        assertThat(result).isEqualTo("SUCCESS");
    }

    @Test
    public void balanceOfTest() throws Exception {

        assertThat(erc721.balanceOf(owner)).isEqualTo("1");
    }

    @Test
    public void ownerOfTest() throws Exception {

        assertThat(erc721.ownerOf(tokenId, owner)).isEqualTo(owner);
    }

    @Test
    public void transferFromTest() throws Exception {

        assertThat(erc721.transfer(owner, newOwner,tokenId)).isEqualTo("SUCCESS");
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
