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

        if(eerc721.mint(tokenId, type, owner, page, hash, signers, path, pathHash).equals("SUCCESS")) {
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

        if(eerc721.divide(tokenId, newTokenIds, values, index, owner).equals("SUCCESS")) {
            Thread.sleep(1000);
            logger.info("divide true");
        } else {
            Thread.sleep(1000);
            logger.info("divide fail");
        }
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

            if(queryOwner.textValue().equals(owner)) {
                logger.info("query owner true");
            }else {
                logger.info("query owner fail");
            }

            if(queryTokenId.textValue().equals(tokenId)) {
                logger.info("query tokenId true");
            }else {
                logger.info("query tokenId fail");
            }

            if(queryType.textValue().equals(type)) {
                logger.info("query type true");
            }else {
                logger.info("query type fail");
            }

            if(querySigners.textValue().equals("[\"" + signers + "\"]")) {
                logger.info("query signers true");
            }else {
                logger.info("query signers fail");
            }

            if(queryHash.textValue().equals(hash)) {
                logger.info("query hash true");
            }else {
                logger.info("query hash fail");
            }

            if(queryValue.textValue().equals(page)) {
                logger.info("query page true");
            }else {
                logger.info("query page fail");
            }

            if(queryPath.textValue().equals(path)) {
                logger.info("query path true");
            }else {
                logger.info("query path fail");
            }

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

            if(queryOwner.textValue().equals(owner)) {
                logger.info("query owner true");
            }else {
                logger.info("query owner fail");
            }

            if(queryTokenId.textValue().equals(newTokenIds[0])) {
                logger.info("query newTokenIds[0] true");
            }else {
                logger.info("query newTokenIds[0] fail");
            }

            if(queryType.textValue().equals(type)) {
                logger.info("query type true");
            }else {
                logger.info("query type fail");
            }

            if(querySigners.textValue().equals("[\"" + signers + "\"]")) {
                logger.info("query signers true");
            }else {
                logger.info("query signers fail");
            }

            if(queryHash.textValue().equals(hash)) {
                logger.info("query hash true");
            }else {
                logger.info("query hash fail");
            }

            if(queryValue.textValue().equals(values[0])) {
                logger.info("query page true");
            }else {
                logger.info("query page fail");
            }

            if(queryPath.textValue().equals(path)) {
                logger.info("query path true");
            }else {
                logger.info("query path fail");
            }

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

            if(queryOwner.textValue().equals(owner)) {
                logger.info("query owner true");
            }else {
                logger.info("query owner fail");
            }

            if(queryTokenId.textValue().equals(newTokenIds[1])) {
                logger.info("query newTokenIds[1] true");
            }else {
                logger.info("query newTokenIds[1] fail");
            }

            if(queryType.textValue().equals(type)) {
                logger.info("query type true");
            }else {
                logger.info("query type fail");
            }

            if(querySigners.textValue().equals("[\"" + signers + "\"]")) {
                logger.info("query signers true");
            }else {
                logger.info("query signers fail");
            }

            if(queryHash.textValue().equals(hash)) {
                logger.info("query hash true");
            }else {
                logger.info("query hash fail");
            }

            if(queryValue.textValue().equals(values[1])) {
                logger.info("query page true");
            }else {
                logger.info("query page fail");
            }

            if(queryPath.textValue().equals(path)) {
                logger.info("query path true");
            }else {
                logger.info("query path fail");
            }

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
        String attr = owner+"SigId";

        if(eerc721.update(tokenId, index, attr, owner).equals("SUCCESS")) {
            Thread.sleep(1000);
            logger.info("update true");
        } else {
            Thread.sleep(1000);
            logger.info("update fail");
        }
    }

    @Test
    public void deactivateTest() throws Exception {

        if(eerc721.deactivate(tokenId, owner).equals("SUCCESS")) {
            Thread.sleep(1000);
            logger.info("deactivate true");
        } else {
            Thread.sleep(1000);
            logger.info("deactivate fail");
        }
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

            if(queryOwner.textValue().equals(owner)) {
                logger.info("query owner true");
            }else {
                logger.info("query owner fail");
            }

            if(queryTokenId.textValue().equals(tokenId)) {
                logger.info("query tokenId true");
            }else {
                logger.info("query tokenId fail");
            }

            if(queryType.textValue().equals(type)) {
                logger.info("query type true");
            }else {
                logger.info("query type fail");
            }

            if(querySigners.textValue().equals("[\"" + signers + "\"]")) {
                logger.info("query signers true");
            }else {
                logger.info("query signers fail");
            }

            if(queryHash.textValue().equals(hash)) {
                logger.info("query hash true");
            }else {
                logger.info("query hash fail");
            }

            if(queryValue.textValue().equals(page)) {
                logger.info("query page true");
            }else {
                logger.info("query page fail");
            }

            if(queryPath.textValue().equals(path)) {
                logger.info("query path true");
            }else {
                logger.info("query path fail");
            }

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
                    logger.info("**************************" + querySigners.textValue());
                    JsonNode queryHash = actualObj_xattr.get("hash");
                    JsonNode queryValue = actualObj_xattr.get("pages");

                    JsonNode queryUri = actualObj.get("uri");
                    JsonNode actualObj_uri = mapper.readTree(queryUri.textValue());
                    JsonNode queryPath = actualObj_uri.get("path");
                    JsonNode queryPath_hash = actualObj_xattr.get("hash");

                    if(queryOwner.textValue().equals(owner)) {
                        logger.info("query owner true");
                    }else {
                        logger.info("query owner fail");
                    }

                    if(queryTokenId.textValue().equals(tokenId)) {
                        logger.info("query tokenId true");
                    }else {
                        logger.info("query tokenId fail");
                    }

                    if(queryType.textValue().equals(type)) {
                        logger.info("query type true");
                    }else {
                        logger.info("query type fail");
                    }

                    if(querySigners.textValue().equals("\"" + signers + "\"")) {
                        logger.info("query signers true");
                    }else {
                        logger.info("query signers fail");
                    }

                    if(queryHash.textValue().equals(hash)) {
                        logger.info("query hash true");
                    }else {
                        logger.info("query hash fail");
                    }

                    if(queryValue.textValue().equals(page)) {
                        logger.info("query page true");
                    }else {
                        logger.info("query page fail");
                    }

                    if(queryPath.textValue().equals(path)) {
                        logger.info("query path true");
                    }else {
                        logger.info("query path fail");
                    }

                    if(queryPath_hash.textValue().equals(pathHash)) {
                        logger.info("query pathHash true");
                    }else {
                        logger.info("query pathHash fail");
                    }
                } else {
                    logger.info("query fail");
                }
            }

//
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode actualObj = mapper.readTree(queryResult);
////
//            JsonNode owner = actualObj.get("owner");
////            JsonNode xattr = actualObj.get("xattr");
////            JsonNode actualObj_xattr = mapper.readTree(xattr.textValue());
////            JsonNode signers = actualObj_xattr.get("signers");
////            JsonNode hash = actualObj_xattr.get("hash");
////            // JsonNode owner = actualObj.get("owner");
////            // JsonNode owner = actualObj.get("owner");
////
//            logger.info(owner.textValue());
//            logger.info(actualObj.toString());
//
//            String jsonString = mapper.writeValueAsString(queryResult);
//            String jsonString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(queryResult);
//            logger.info(jsonString2);
//
//            actualObj = mapper.readTree(jsonString2);
//            logger.info(actualObj.toString());

//            Iterator<JsonNode> owner2 = actualObj.iterator();
//            while(owner2.hasNext()) {
//                owner2.
//            }

            //JSONArray a = (JSONArray)new JSONObject(queryResult);
//            logger.info(signers.textValue());
//            logger.info(hash.textValue());
        }
    }

}
