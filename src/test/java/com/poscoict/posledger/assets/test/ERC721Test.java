package com.poscoict.posledger.assets.test;

import com.poscoict.posledger.assets.org.chaincode.ERC721.ERC721;
import com.poscoict.posledger.assets.org.chaincode.EnrollmentUser;
import com.poscoict.posledger.assets.service.RedisService;
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
    String owner = "alice";
    String newOwner = "bob";
    String approved = "carol";
    String operator = "david";
    String tokenId = "0";

    @Autowired
    RedisService redisService;

    @Test
    public void enrollTest() throws Exception {
        EnrollmentUser enrollToCA = new EnrollmentUser();

        enrollToCA.enrollAdmin();
        String certificate = enrollToCA.registerUser(owner);
        redisService.storeUser(owner, certificate);

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
