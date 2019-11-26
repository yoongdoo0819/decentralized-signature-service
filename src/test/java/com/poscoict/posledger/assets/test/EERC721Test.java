package com.poscoict.posledger.assets.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.assertj.core.api.Assertions.assertThat;

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
    String newTokenIds[] = {"2", "3"};
    String type = "doc";
    String page = "100";
    String hash = "doc";
    String signers = owner;
    String path = "doc";
    String pathHash = "doc";
    String values[] = {"40", "60"};
    int index = 3;

    @Test
    public void mintTest() throws Exception {

        assertThat(eerc721.mint(tokenId, type, owner, page, hash, signers, path, pathHash)).isEqualTo("SUCCESS");
    }

    @Test
    public void balanceOfTest() throws Exception {

        assertThat(eerc721.balanceOf(owner, type)).isEqualTo("1");
    }

    @Test
    public void divideTest() throws Exception {

        assertThat(eerc721.divide(tokenId, newTokenIds, values, index, owner)).isEqualTo("SUCCESS");
    }

    @Test
    public void queryTest() throws Exception {
        String queryResult = eerc721.query(tokenId, owner);

        if(queryResult != null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(queryResult);

            JsonNode queryOwner = actualObj.get("owner");
            JsonNode queryTokenId = actualObj.get("id");
            JsonNode queryType = actualObj.get("type");

            JsonNode queryXattr = actualObj.get("xattr");
            JsonNode actualObj_xattr = mapper.readTree(queryXattr.textValue());
            JsonNode querySigners = actualObj_xattr.get("signers");
            JsonNode queryHash = actualObj_xattr.get("hash");
            JsonNode queryValue = actualObj_xattr.get("pages");

            JsonNode queryUri = actualObj.get("uri");
            JsonNode actualObj_uri = mapper.readTree(queryUri.textValue());
            JsonNode queryPath = actualObj_uri.get("path");
            JsonNode queryPath_hash = actualObj_xattr.get("hash");

            assertThat(queryOwner.textValue()).isEqualTo(owner);
            if(queryOwner.textValue().equals(owner)) {
                logger.info("query owner true");
            }else {
                logger.info("query owner fail");
            }

            assertThat(queryTokenId.textValue()).isEqualTo(tokenId);
            if(queryTokenId.textValue().equals(tokenId)) {
                logger.info("query tokenId true");
            }else {
                logger.info("query tokenId fail");
            }

            assertThat(queryType.textValue()).isEqualTo(type);
            if(queryType.textValue().equals(type)) {
                logger.info("query type true");
            }else {
                logger.info("query type fail");
            }

            assertThat(querySigners.textValue()).isEqualTo("[\"" + signers + "\"]");
            if(querySigners.textValue().equals("[\"" + signers + "\"]")) {
                logger.info("query signers true");
            }else {
                logger.info("query signers fail");
            }

            assertThat(queryHash.textValue()).isEqualTo(hash);
            if(queryHash.textValue().equals(hash)) {
                logger.info("query hash true");
            }else {
                logger.info("query hash fail");
            }

            assertThat(queryValue.textValue()).isEqualTo(page);
            if(queryValue.textValue().equals(page)) {
                logger.info("query page true");
            }else {
                logger.info("query page fail");
            }

            assertThat(queryPath.textValue()).isEqualTo(path);
            if(queryPath.textValue().equals(path)) {
                logger.info("query path true");
            }else {
                logger.info("query path fail");
            }

            assertThat(queryPath_hash.textValue()).isEqualTo(pathHash);
            if(queryPath_hash.textValue().equals(pathHash)) {
                logger.info("query pathHash true");
            }else {
                logger.info("query pathHash fail");
            }
        } else {
            logger.info("query fail");
        }
    }

    @Test
    public void queryNewToken0Test() throws Exception {
        String queryResult = eerc721.query(newTokenIds[0], owner);

        if(queryResult != null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(queryResult);

            JsonNode queryOwner = actualObj.get("owner");
            JsonNode queryTokenId = actualObj.get("id");
            JsonNode queryType = actualObj.get("type");

            JsonNode queryXattr = actualObj.get("xattr");
            JsonNode actualObj_xattr = mapper.readTree(queryXattr.textValue());
            JsonNode querySigners = actualObj_xattr.get("signers");
            JsonNode queryHash = actualObj_xattr.get("hash");
            JsonNode queryValue = actualObj_xattr.get("pages");

            JsonNode queryUri = actualObj.get("uri");
            JsonNode actualObj_uri = mapper.readTree(queryUri.textValue());
            JsonNode queryPath = actualObj_uri.get("path");
            JsonNode queryPath_hash = actualObj_xattr.get("hash");

            assertThat(queryOwner.textValue()).isEqualTo(owner);
            if(queryOwner.textValue().equals(owner)) {
                logger.info("query owner true");
            }else {
                logger.info("query owner fail");
            }

            assertThat(queryTokenId.textValue()).isEqualTo(newTokenIds[0]);
            if(queryTokenId.textValue().equals(newTokenIds[0])) {
                logger.info("query newTokenIds[0] true");
            }else {
                logger.info("query newTokenIds[0] fail");
            }

            assertThat(queryType.textValue()).isEqualTo(type);
            if(queryType.textValue().equals(type)) {
                logger.info("query type true");
            }else {
                logger.info("query type fail");
            }

            assertThat(querySigners.textValue()).isEqualTo("[\"" + signers + "\"]");
            if(querySigners.textValue().equals("[\"" + signers + "\"]")) {
                logger.info("query signers true");
            }else {
                logger.info("query signers fail");
            }

            assertThat(queryHash.textValue()).isEqualTo(hash);
            if(queryHash.textValue().equals(hash)) {
                logger.info("query hash true");
            }else {
                logger.info("query hash fail");
            }

            assertThat(queryValue.textValue()).isEqualTo(values[0]);
            if(queryValue.textValue().equals(values[0])) {
                logger.info("query page true");
            }else {
                logger.info("query page fail");
            }

            assertThat(queryPath.textValue()).isEqualTo(path);
            if(queryPath.textValue().equals(path)) {
                logger.info("query path true");
            }else {
                logger.info("query path fail");
            }

            assertThat(queryPath_hash.textValue()).isEqualTo(pathHash);
            if(queryPath_hash.textValue().equals(pathHash)) {
                logger.info("query pathHash true");
            }else {
                logger.info("query pathHash fail");
            }
        } else {
            logger.info("query fail");
        }
    }

    @Test
    public void queryNewToken1Test() throws Exception {
        String queryResult = eerc721.query(newTokenIds[1], owner);

        if(queryResult != null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(queryResult);

            JsonNode queryOwner = actualObj.get("owner");
            JsonNode queryTokenId = actualObj.get("id");
            JsonNode queryType = actualObj.get("type");

            JsonNode queryXattr = actualObj.get("xattr");
            JsonNode actualObj_xattr = mapper.readTree(queryXattr.textValue());
            JsonNode querySigners = actualObj_xattr.get("signers");
            JsonNode queryHash = actualObj_xattr.get("hash");
            JsonNode queryValue = actualObj_xattr.get("pages");

            JsonNode queryUri = actualObj.get("uri");
            JsonNode actualObj_uri = mapper.readTree(queryUri.textValue());
            JsonNode queryPath = actualObj_uri.get("path");
            JsonNode queryPath_hash = actualObj_xattr.get("hash");

            assertThat(queryOwner.textValue()).isEqualTo(owner);
            if(queryOwner.textValue().equals(owner)) {
                logger.info("query owner true");
            }else {
                logger.info("query owner fail");
            }

            assertThat(queryTokenId.textValue()).isEqualTo(newTokenIds[1]);
            if(queryTokenId.textValue().equals(newTokenIds[1])) {
                logger.info("query newTokenIds[0] true");
            }else {
                logger.info("query newTokenIds[0] fail");
            }

            assertThat(queryType.textValue()).isEqualTo(type);
            if(queryType.textValue().equals(type)) {
                logger.info("query type true");
            }else {
                logger.info("query type fail");
            }

            assertThat(querySigners.textValue()).isEqualTo("[\"" + signers + "\"]");
            if(querySigners.textValue().equals("[\"" + signers + "\"]")) {
                logger.info("query signers true");
            }else {
                logger.info("query signers fail");
            }

            assertThat(queryHash.textValue()).isEqualTo(hash);
            if(queryHash.textValue().equals(hash)) {
                logger.info("query hash true");
            }else {
                logger.info("query hash fail");
            }

            assertThat(queryValue.textValue()).isEqualTo(values[1]);
            if(queryValue.textValue().equals(values[1])) {
                logger.info("query page true");
            }else {
                logger.info("query page fail");
            }

            assertThat(queryPath.textValue()).isEqualTo(path);
            if(queryPath.textValue().equals(path)) {
                logger.info("query path true");
            }else {
                logger.info("query path fail");
            }

            assertThat(queryPath_hash.textValue()).isEqualTo(pathHash);
            if(queryPath_hash.textValue().equals(pathHash)) {
                logger.info("query pathHash true");
            }else {
                logger.info("query pathHash fail");
            }
        } else {
            logger.info("query fail");
        }
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
        String attr = owner+" SigId";

        assertThat(eerc721.update(tokenId, index, attr, owner)).isEqualTo("SUCCESS");
    }

    @Test
    public void deactivateTest() throws Exception {

        assertThat(eerc721.deactivate(tokenId, owner)).isEqualTo("SUCCESS");
    }

    @Test
    public void afterUpdateAndDeactivateQueryTest() throws Exception {
        String queryResult = eerc721.query(tokenId, owner);

        if(queryResult != null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(queryResult);

            JsonNode queryOwner = actualObj.get("owner");
            JsonNode queryTokenId = actualObj.get("id");
            JsonNode queryType = actualObj.get("type");

            JsonNode queryXattr = actualObj.get("xattr");
            JsonNode actualObj_xattr = mapper.readTree(queryXattr.textValue());
            JsonNode querySigners = actualObj_xattr.get("signers");
            JsonNode queryHash = actualObj_xattr.get("hash");
            JsonNode queryValue = actualObj_xattr.get("pages");

            JsonNode queryUri = actualObj.get("uri");
            JsonNode actualObj_uri = mapper.readTree(queryUri.textValue());
            JsonNode queryPath = actualObj_uri.get("path");
            JsonNode queryPath_hash = actualObj_xattr.get("hash");

            assertThat(queryOwner.textValue()).isEqualTo(owner);

            if(queryOwner.textValue().equals(owner)) {
                logger.info("query owner true");
            }else {
                logger.info("query owner fail");
            }

            assertThat(queryTokenId.textValue()).isEqualTo(tokenId);
            if(queryTokenId.textValue().equals(tokenId)) {
                logger.info("query tokenId true");
            }else {
                logger.info("query tokenId fail");
            }

            assertThat(queryType.textValue()).isEqualTo(type);
            if(queryType.textValue().equals(type)) {
                logger.info("query type true");
            }else {
                logger.info("query type fail");
            }

            assertThat(querySigners.textValue()).isEqualTo("[\"" + signers + "\"]");
            if(querySigners.textValue().equals("[\"" + signers + "\"]")) {
                logger.info("query signers true");
            }else {
                logger.info("query signers fail");
            }

            assertThat(queryHash.textValue()).isEqualTo(hash);
            if(queryHash.textValue().equals(hash)) {
                logger.info("query hash true");
            }else {
                logger.info("query hash fail");
            }

            assertThat(queryValue.textValue()).isEqualTo(page);
            if(queryValue.textValue().equals(page)) {
                logger.info("query page true");
            }else {
                logger.info("query page fail");
            }

            assertThat(queryPath.textValue()).isEqualTo(path);
            if(queryPath.textValue().equals(path)) {
                logger.info("query path true");
            }else {
                logger.info("query path fail");
            }

            assertThat(queryPath_hash.textValue()).isEqualTo(pathHash);
            if(queryPath_hash.textValue().equals(pathHash)) {
                logger.info("query pathHash true");
            }else {
                logger.info("query pathHash fail");
            }
        } else {
            logger.info("query fail");
        }
    }

    @Test
    public void queryHistoryTest() throws Exception {
        String queryResult = eerc721.queryHistory(tokenId, owner);
        if(queryResult != null) {
            String[] queryArray = queryResult.replace("[", "").replace("]", "").split(", ");

            logger.info(queryArray[0]);
            for(int i=0; i<queryArray.length; i++) {
                if(queryArray[i] != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode actualObj = mapper.readTree(queryArray[i]);

                    JsonNode queryOwner = actualObj.get("owner");
                    JsonNode queryTokenId = actualObj.get("id");
                    JsonNode queryType = actualObj.get("type");

                    JsonNode queryXattr = actualObj.get("xattr");
                    JsonNode actualObj_xattr = mapper.readTree(queryXattr.textValue());
                    JsonNode querySigners = actualObj_xattr.get("signers");
                    JsonNode queryHash = actualObj_xattr.get("hash");
                    JsonNode queryValue = actualObj_xattr.get("pages");

                    JsonNode queryUri = actualObj.get("uri");
                    JsonNode actualObj_uri = mapper.readTree(queryUri.textValue());
                    JsonNode queryPath = actualObj_uri.get("path");
                    JsonNode queryPath_hash = actualObj_xattr.get("hash");

                    assertThat(queryOwner.textValue()).isEqualTo(owner);

                    if(queryOwner.textValue().equals(owner)) {
                        logger.info("query owner true");
                    }else {
                        logger.info("query owner fail");
                    }

                    assertThat(queryTokenId.textValue()).isEqualTo(tokenId);
                    if(queryTokenId.textValue().equals(tokenId)) {
                        logger.info("query tokenId true");
                    }else {
                        logger.info("query tokenId fail");
                    }

                    assertThat(queryType.textValue()).isEqualTo(type);
                    if(queryType.textValue().equals(type)) {
                        logger.info("query type true");
                    }else {
                        logger.info("query type fail");
                    }

                    assertThat(querySigners.textValue()).isEqualTo("\"" + signers + "\"");
                    if(querySigners.textValue().equals("[\"" + signers + "\"]")) {
                        logger.info("query signers true");
                    }else {
                        logger.info("query signers fail");
                    }

                    assertThat(queryHash.textValue()).isEqualTo(hash);
                    if(queryHash.textValue().equals(hash)) {
                        logger.info("query hash true");
                    }else {
                        logger.info("query hash fail");
                    }

                    assertThat(queryValue.textValue()).isEqualTo(page);
                    if(queryValue.textValue().equals(page)) {
                        logger.info("query page true");
                    }else {
                        logger.info("query page fail");
                    }

                    assertThat(queryPath.textValue()).isEqualTo(path);
                    if(queryPath.textValue().equals(path)) {
                        logger.info("query path true");
                    }else {
                        logger.info("query path fail");
                    }

                    assertThat(queryPath_hash.textValue()).isEqualTo(pathHash);
                    if(queryPath_hash.textValue().equals(pathHash)) {
                        logger.info("query pathHash true");
                    }else {
                        logger.info("query pathHash fail");
                    }
                } else {
                    logger.info("query fail");
                }
            }

        }
    }

}
