package com.poscoict.posledger.assets.test;

import com.poscoict.posledger.assets.org.chaincode.EERC721.EERC721;
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
public class EERC721Test {

    @Autowired
    private EERC721 eerc721;

    private static final Logger logger = LoggerFactory.getLogger(EERC721Test.class);

    String owner = "alice";
    String tokenId = "1";
    String newTokenId = "2";
    String type = "doc";
    String hash = "doc";
    String signers = "";
    String path = "doc";
    String pathHash = "doc";

    @Test
    public void mintTest() throws Exception {

        if(eerc721.mint(tokenId, type, owner, hash, signers, path, pathHash).equals("SUCCESS")) {
            Thread.sleep(1000);
            logger.info("mint true");
        } else {
            Thread.sleep(1000);
            logger.info("mint fail");
        }
    }

    @Test
    public void balanceOfTest() throws Exception {

        if(eerc721.balanceOf(owner, type).equals("1")) {
            Thread.sleep(1000);
            logger.info("balanceOf true");
        } else {
            Thread.sleep(1000);
            logger.info("balanceOf fail");
        }
    }

    @Test
    public void divideTest() throws Exception {

        if(eerc721.divide(tokenId, newTokenId).equals("SUCCESS")) {
            Thread.sleep(1000);
            logger.info("divide true");
        } else {
            Thread.sleep(1000);
            logger.info("divide fail");
        }
    }

    @Test
    public void queryTest() throws Exception {
        logger.info(eerc721.query(tokenId));
    }

    @Test
    public void queryNewTokenTest() throws Exception {
        logger.info(eerc721.query(newTokenId));
    }


    /*
        attr      | index
        ==================
        hash      | 0
        signers   | 1
        sigIds    | 2
        activated | 3
    */
    @Test
    public void updateTest() throws Exception {

        String index = "2";
        String attr = owner+"SigId";

        if(eerc721.update(tokenId, index, attr).equals("SUCCESS")) {
            Thread.sleep(1000);
            logger.info("update true");
        } else {
            Thread.sleep(1000);
            logger.info("update fail");
        }
    }

    @Test
    public void deactivateTest() throws Exception {

        if(eerc721.deactivate(tokenId).equals("SUCCESS")) {
            Thread.sleep(1000);
            logger.info("deactivate true");
        } else {
            Thread.sleep(1000);
            logger.info("deactivate fail");
        }
    }

    @Test
    public void afterUpdateAndDeactivateQueryTest() throws Exception {
        logger.info(eerc721.query(tokenId));
    }

    @Test
    public void queryHistoryTest() throws Exception {
        logger.info(eerc721.queryHistory(tokenId));
    }

}
