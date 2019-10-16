package com.poscoict.posledger.assets.test;

import com.poscoict.posledger.assets.org.chaincode.ERC721.ERC721;
import com.poscoict.posledger.assets.org.chaincode.EnrollmentUser;
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
    String owner = "alice";
    String newOwner = "bob";
    String approved = "carol";
    String operator = "david";
    String tokenId = "0";

    @Test
    public void registerTest() throws Exception {
        EnrollmentUser enrollToCA = new EnrollmentUser();

        enrollToCA.enrollAdmin();
        enrollToCA.registerUser(owner);
        enrollToCA.registerUser(newOwner);
    }

    @Test
    public void mintTest() throws Exception {

        if(erc721.mint(tokenId, owner).equals("SUCCESS")) {
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

        if(erc721.ownerOf(tokenId).equals(owner)) {
            Thread.sleep(1000);
            logger.info("ownerOf true");
        } else {
            Thread.sleep(1000);
            logger.info("ownerOf fail");
        }
    }

    @Test
    public void transferFromTest() throws Exception {

        if(erc721.transferToken(owner, newOwner, tokenId).equals("SUCCESS")) {
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

        if(erc721.ownerOf(tokenId).equals(newOwner)) {
            Thread.sleep(1000);
            logger.info("ownerOf true");
        } else {
            Thread.sleep(1000);
            logger.info("ownerOf fail");
        }
    }

    @Test
    public void approveTest() throws Exception {

        if(erc721.approve(approved, tokenId).equals("SUCCESS")) {
            Thread.sleep(1000);
            logger.info("approve true");
        } else {
            Thread.sleep(1000);
            logger.info("approve fail");
        }
    }

    @Test
    public void getApprovedTest() throws Exception {

        if(erc721.getApproved(tokenId).equals(approved)) {
            Thread.sleep(1000);
            logger.info("getApprove true");
        } else {
            Thread.sleep(1000);
            logger.info("getApprove fail");
        }
    }

    @Test
    public void setApprovedForAllTest() throws Exception {

        if(erc721.setApprovedForAll(newOwner, operator, "true").equals("SUCCESS")) {
            Thread.sleep(1000);
            logger.info("setApprovedForAll true");
        } else {
            Thread.sleep(1000);
            logger.info("setApprovedForAll fail");
        }
    }

    @Test
    public void isApprovedForAllTest() throws Exception {

        if(erc721.isApprovedForAll(newOwner, operator).equals("TRUE")) {
            Thread.sleep(1000);
            logger.info("isApprovedForAll true");
        } else {
            Thread.sleep(1000);
            logger.info("isApprovedForAll fail");
        }
    }

}
