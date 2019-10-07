package com.poscoict.posledger.assets.test;

import com.poscoict.posledger.assets.TokenIssuer;
import com.poscoict.posledger.assets.org.app.chaincode.invocation.mintDocNFT;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class springTest {

    private static final Logger logger = LoggerFactory.getLogger(TokenIssuerTest.class);

    @Autowired
    private TokenIssuer tokenResolver;

    @Test
    public void mintDocNFTTest() throws Exception {

        mintDocNFT mint = new mintDocNFT();
        assertEquals("asd", mint.mint(0, "owner", "docid", "signers", "path", "pathHash"));

    }
}
