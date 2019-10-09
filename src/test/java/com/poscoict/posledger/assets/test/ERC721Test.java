package com.poscoict.posledger.assets.test;

import com.poscoict.posledger.assets.org.app.chaincode.invocation.ERC721.ERC721;
import com.poscoict.posledger.assets.org.app.chaincode.invocation.registerUser;
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
public class ERC721Test {

    @Autowired
    private ERC721 erc721;

    private static final Logger logger = LoggerFactory.getLogger(ERC721Test.class);
    String owner = "test";


    @Test
    public void mintTest() throws Exception {

        registerUser registeruser = new registerUser();
        registeruser.registerNewUser(owner);

        if(erc721.mint("0", owner).equals("Success")) {
            Thread.sleep(1000);
            logger.info("mint true");
        } else {
            Thread.sleep(1000);
            logger.info("mint fail");
        }
    }

    @Test
    public void balanceOfTest() throws Exception {

        if(erc721.balanceOf(owner).equals("1")) {
            Thread.sleep(1000);
            logger.info("balanceOf true");
        } else {
            Thread.sleep(1000);
            logger.info("balanceOf fail");
        }
    }

    @Test
    public void ownerOfTest() throws Exception {

        if(erc721.ownerOf("0").equals(owner)) {
            Thread.sleep(1000);
            logger.info("ownerOf true");
        } else {
            Thread.sleep(1000);
            logger.info("ownerOf fail");
        }
    }

    @Test
    public void approveTest() throws Exception {

        if(erc721.approve("approved", "0").equals("Success")) {
            Thread.sleep(1000);
            logger.info("approve true");
        } else {
            Thread.sleep(1000);
            logger.info("approve fail");
        }
    }

    @Test
    public void getApprovedTest() throws Exception {

        if(erc721.getApproved("0").equals("approved")) {
            Thread.sleep(1000);
            logger.info("getApprove true");
        } else {
            Thread.sleep(1000);
            logger.info("getApprove fail");
        }
    }

    @Test
    public void setApprovedForAllTest() throws Exception {

        if(erc721.setApprovedForAll(owner, "operator", "true").equals("operator is added to operator of the token owned by test")) {
            Thread.sleep(1000);
            logger.info("setApprovedForAll true");
        } else {
            Thread.sleep(1000);
            logger.info("setApprovedForAll fail");
        }
    }

    @Test
    public void isApprovedForAllTest() throws Exception {

        if(erc721.isApprovedForAll(owner, "operator").equals("true")) {
            Thread.sleep(1000);
            logger.info("isApprovedForAll true");
        } else {
            Thread.sleep(1000);
            logger.info("isApprovedForAll fail");
        }
    }

    @Test
    public void transferFromTest() throws Exception {

        if(erc721.transferToken(owner, "receiver", "0").equals("Success")) {
            Thread.sleep(1000);
            logger.info("transferFrom true");
        } else {
            Thread.sleep(1000);
            logger.info("transferFrom fail");
        }
    }

    @Test
    public void afterThatBalanceOfTest() throws Exception {

        if(erc721.balanceOf(owner).equals("0")) {
            Thread.sleep(1000);
            logger.info("balanceOf true");
        } else {
            Thread.sleep(1000);
            logger.info("balanceOf fail");
        }
    }

    @Test
    public void afterThatOwnerOfTest() throws Exception {

        if(erc721.ownerOf("0").equals("receiver")) {
            Thread.sleep(1000);
            logger.info("ownerOf true");
        } else {
            Thread.sleep(1000);
            logger.info("ownerOf fail");
        }
    }
}
