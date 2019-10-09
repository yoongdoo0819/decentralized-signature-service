package com.poscoict.posledger.assets.test;

import com.poscoict.posledger.assets.org.app.chaincode.invocation.EERC721.EERC721;
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
    String owner = "test";


    @Test
    public void mintTest() throws Exception {

        if(eerc721.balanceOf("0", owner).equals("Success")) {
            Thread.sleep(1000);
            logger.info("mint true");
        } else {
            Thread.sleep(1000);
            logger.info("mint fail");
        }
    }

    @Test
    public void balanceOfTest() throws Exception {

        if(eerc721.balanceOf(owner, "base").equals("0")) {
            Thread.sleep(1000);
            logger.info("balanceOf true");
        } else {
            Thread.sleep(1000);
            logger.info("balanceOf fail");
        }
    }

    @Test
    public void divideTest() throws Exception {

        if(eerc721.divide("0", "1").equals("")) {
            Thread.sleep(1000);
            logger.info("divide true");
        } else {
            Thread.sleep(1000);
            logger.info("divide fail");
        }
    }

    @Test
    public void deactivateTest() throws Exception {

        if(eerc721.deactivate("0").equals("")) {
            Thread.sleep(1000);
            logger.info("deactivate true");
        } else {
            Thread.sleep(1000);
            logger.info("deactivate fail");
        }
    }

    @Test
    public void queryTest() throws Exception {
        logger.info(eerc721.query("0"));
    }

    @Test
    public void queryHistoryTest() throws Exception {
        logger.info(eerc721.queryHistory("0"));
    }

}
