package com.poscoict.posledger.assets.test;

import assets.chaincode.EERC721.EERC721;
import assets.config.UserConfig;
import assets.user.UserContext;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscoict.posledger.assets.org.chaincode.AddressUtils;
import com.poscoict.posledger.assets.org.chaincode.RedisEnrollment;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.identity.X509Identity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

//import com.poscoict.posledger.assets.org.chaincode.EERC721.EERC721;
//import com.poscoict.posledger.assets.org.chaincode.UserConfig;
//import com.poscoict.posledger.assets.org.user.UserContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class EERC721Test {

    //@Autowired
    private EERC721 eerc721 = new EERC721();

    @Autowired
    RedisEnrollment re;

//    @Autowired
//    UserConfig UserConfig;

    private static final Logger logger = LoggerFactory.getLogger(EERC721Test.class);

    String owner = "alice";
    String tokenId = "1";
    String newTokenIds[] = {"2", "3"};
    String type = "doc";
    int pages = 100;
    String hash = "doc";
    String signers = owner;
    String path = "doc";
    String pathHash = "doc";
    String values[] = {"40", "60"};
    int index = 3;

    @Test
    public void registerTest() throws Exception {

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(owner);
        UserConfig.setEnrollment(owner, enrollment);

        assertThat(eerc721.register(tokenId, type, owner, pages, hash, signers, path, pathHash)).isEqualTo("SUCCESS");
    }

    @Test
    public void balanceOfTest() throws Exception {

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(owner);
        UserConfig.setEnrollment(owner, enrollment);

        assertThat(eerc721.balanceOf(owner, type)).isEqualTo("1");
    }

    @Test
    public void divideTest() throws Exception {

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(owner);
        UserConfig.setEnrollment(owner, enrollment);

        assertThat(eerc721.divide(tokenId, newTokenIds, values, index)).isEqualTo("SUCCESS");
    }

    @Test
    public void queryTest() throws Exception {

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(owner);
        UserConfig.setEnrollment(owner, enrollment);

        UserContext userContext = UserConfig.initUserContextForOwner();
        X509Identity identity = new X509Identity(userContext);
        String addr = AddressUtils.getMyAddress(identity);

        String queryResult = eerc721.query(tokenId);

        if(queryResult != null) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map =
                    mapper.readValue(queryResult, new TypeReference<HashMap<String, Object>>(){});

            String type = (String) map.get("type");
            String owner = (String) map.get("owner");
            String approvee = (String) map.get("approvee");
            Map<String, Object> xattrMap = (HashMap<String, Object>) map.get("xattr");

            List<String> signers = (ArrayList<String>) xattrMap.get("signers");
            String hash = (String) xattrMap.get("hash");
            int pages = (int) xattrMap.get("pages");

            Map<String, String> uriMap = (HashMap<String, String>) map.get("uri");
            String path = uriMap.get("path");
            String merkleroot = uriMap.get("hash");

            assertThat(owner.equals(addr));
            if(owner.equals(addr)) {
                logger.info("query owner true");
            }else {
                logger.info("query owner fail");
            }


            assertThat(type.equals(this.type));
            if(type.equals(this.type)) {
                logger.info("query type true");
            }else {
                logger.info("query type fail");
            }

            assertThat(signers.get(0).equals(this.signers));
            if(signers.get(0).equals(this.signers)) {
                logger.info("query signers true");
            }else {
                logger.info("query signers fail");
            }

            assertThat(hash.equals(this.hash));
            if(hash.equals(this.hash)) {
                logger.info("query hash true");
            }else {
                logger.info("query hash fail");
            }

            assertThat(pages == this.pages);
            if(pages == this.pages) {
                logger.info("query page true");
            }else {
                logger.info("query page fail");
            }

            assertThat(path.equals(this.path));
            if(path.equals(this.path)) {
                logger.info("query path true");
            }else {
                logger.info("query path fail");
            }

            assertThat(merkleroot.equals(this.pathHash));
            if(merkleroot.equals(this.pathHash)) {
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

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(owner);
        UserConfig.setEnrollment(owner, enrollment);

        UserContext userContext = UserConfig.initUserContextForOwner();
        X509Identity identity = new X509Identity(userContext);
        String addr = AddressUtils.getMyAddress(identity);

        String queryResult = eerc721.query(newTokenIds[0]);

        if(queryResult != null) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map =
                    mapper.readValue(queryResult, new TypeReference<HashMap<String, Object>>(){});

            String type = (String) map.get("type");
            String owner = (String) map.get("owner");
            String approvee = (String) map.get("approvee");
            Map<String, Object> xattrMap = (HashMap<String, Object>) map.get("xattr");

            List<String> signers = (ArrayList<String>) xattrMap.get("signers");
            String hash = (String) xattrMap.get("hash");
            int pages = (int) xattrMap.get("pages");

            Map<String, String> uriMap = (HashMap<String, String>) map.get("uri");
            String path = uriMap.get("path");
            String merkleroot = uriMap.get("hash");

            assertThat(owner.equals(addr));
            if(owner.equals(addr)) {
                logger.info("query owner true");
            }else {
                logger.info("query owner fail");
            }


            assertThat(type.equals(this.type));
            if(type.equals(this.type)) {
                logger.info("query type true");
            }else {
                logger.info("query type fail");
            }

            assertThat(signers.get(0).equals(this.signers));
            if(signers.get(0).equals(this.signers)) {
                logger.info("query signers true");
            }else {
                logger.info("query signers fail");
            }

            assertThat(hash.equals(this.hash));
            if(hash.equals(this.hash)) {
                logger.info("query hash true");
            }else {
                logger.info("query hash fail");
            }

            assertThat(pages == this.pages);
            if(pages == this.pages) {
                logger.info("query page true");
            }else {
                logger.info("query page fail");
            }

            assertThat(path.equals(this.path));
            if(path.equals(this.path)) {
                logger.info("query path true");
            }else {
                logger.info("query path fail");
            }

            assertThat(merkleroot.equals(this.pathHash));
            if(merkleroot.equals(this.pathHash)) {
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

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(owner);
        UserConfig.setEnrollment(owner, enrollment);

        UserContext userContext = UserConfig.initUserContextForOwner();
        X509Identity identity = new X509Identity(userContext);
        String addr = AddressUtils.getMyAddress(identity);

        String queryResult = eerc721.query(newTokenIds[1]);

        if(queryResult != null) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map =
                    mapper.readValue(queryResult, new TypeReference<HashMap<String, Object>>(){});

            String type = (String) map.get("type");
            String owner = (String) map.get("owner");
            String approvee = (String) map.get("approvee");
            Map<String, Object> xattrMap = (HashMap<String, Object>) map.get("xattr");

            List<String> signers = (ArrayList<String>) xattrMap.get("signers");
            String hash = (String) xattrMap.get("hash");
            int pages = (int) xattrMap.get("pages");

            Map<String, String> uriMap = (HashMap<String, String>) map.get("uri");
            String path = uriMap.get("path");
            String merkleroot = uriMap.get("hash");

            assertThat(owner.equals(addr));
            if(owner.equals(addr)) {
                logger.info("query owner true");
            }else {
                logger.info("query owner fail");
            }


            assertThat(type.equals(this.type));
            if(type.equals(this.type)) {
                logger.info("query type true");
            }else {
                logger.info("query type fail");
            }

            assertThat(signers.get(0).equals(this.signers));
            if(signers.get(0).equals(this.signers)) {
                logger.info("query signers true");
            }else {
                logger.info("query signers fail");
            }

            assertThat(hash.equals(this.hash));
            if(hash.equals(this.hash)) {
                logger.info("query hash true");
            }else {
                logger.info("query hash fail");
            }

            assertThat(pages == this.pages);
            if(pages == this.pages) {
                logger.info("query page true");
            }else {
                logger.info("query page fail");
            }

            assertThat(path.equals(this.path));
            if(path.equals(this.path)) {
                logger.info("query path true");
            }else {
                logger.info("query path fail");
            }

            assertThat(merkleroot.equals(this.pathHash));
            if(merkleroot.equals(this.pathHash)) {
                logger.info("query pathHash true");
            }else {
                logger.info("query pathHash fail");
            }
        } else {
            logger.info("query fail");
        }
    }

    @Test
    public void updateTest() throws Exception {

        String index = "2";
        String attr = owner+" SigId";

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(owner);
        UserConfig.setEnrollment(owner, enrollment);

        assertThat(eerc721.update(tokenId, "sigIds", attr)).isEqualTo("SUCCESS");
    }

    @Test
    public void deactivateTest() throws Exception {

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(owner);
        UserConfig.setEnrollment(owner, enrollment);

        assertThat(eerc721.deactivate(tokenId)).isEqualTo("SUCCESS");
    }

    @Test
    public void afterUpdateAndDeactivateQueryTest() throws Exception {

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(owner);
        UserConfig.setEnrollment(owner, enrollment);

        UserContext userContext = UserConfig.initUserContextForOwner();
        X509Identity identity = new X509Identity(userContext);
        String addr = AddressUtils.getMyAddress(identity);

        String queryResult = eerc721.query(tokenId);

        if(queryResult != null) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map =
                    mapper.readValue(queryResult, new TypeReference<HashMap<String, Object>>(){});

            String type = (String) map.get("type");
            String owner = (String) map.get("owner");
            String approvee = (String) map.get("approvee");
            Map<String, Object> xattrMap = (HashMap<String, Object>) map.get("xattr");

            List<String> signers = (ArrayList<String>) xattrMap.get("signers");
            String hash = (String) xattrMap.get("hash");
            int pages = (int) xattrMap.get("pages");

            Map<String, String> uriMap = (HashMap<String, String>) map.get("uri");
            String path = uriMap.get("path");
            String merkleroot = uriMap.get("hash");

            assertThat(owner.equals(addr));
            if(owner.equals(addr)) {
                logger.info("query owner true");
            }else {
                logger.info("query owner fail");
            }


            assertThat(type.equals(this.type));
            if(type.equals(this.type)) {
                logger.info("query type true");
            }else {
                logger.info("query type fail");
            }

            assertThat(signers.get(0).equals(this.signers));
            if(signers.get(0).equals(this.signers)) {
                logger.info("query signers true");
            }else {
                logger.info("query signers fail");
            }

            assertThat(hash.equals(this.hash));
            if(hash.equals(this.hash)) {
                logger.info("query hash true");
            }else {
                logger.info("query hash fail");
            }

            assertThat(pages == this.pages);
            if(pages == this.pages) {
                logger.info("query page true");
            }else {
                logger.info("query page fail");
            }

            assertThat(path.equals(this.path));
            if(path.equals(this.path)) {
                logger.info("query path true");
            }else {
                logger.info("query path fail");
            }

            assertThat(merkleroot.equals(this.pathHash));
            if(merkleroot.equals(this.pathHash)) {
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

        //UserConfig.initUserContext(owner);
        Enrollment enrollment = re.getEnrollment(owner);
        UserConfig.setEnrollment(owner, enrollment);

        UserContext userContext = UserConfig.initUserContextForOwner();
        X509Identity identity = new X509Identity(userContext);
        String addr = AddressUtils.getMyAddress(identity);

        String queryResult = eerc721.queryHistory(tokenId, owner);
        queryResult = queryResult.substring(1, queryResult.length());
        queryResult = queryResult.substring(0, queryResult.length() - 1);

        if (queryResult != null) {
            String[] queryArray = queryResult.split(", ");

            for (int i = 0; i < queryArray.length; i++) {
                if (queryArray[i] != null) {

                    ObjectMapper mapper = new ObjectMapper();

                    Map<String, Object> map =
                            mapper.readValue(queryArray[i], new TypeReference<HashMap<String, Object>>() {
                            });

                    String type = (String) map.get("type");
                    String owner = (String) map.get("owner");
                    String approvee = (String) map.get("approvee");
                    Map<String, Object> xattrMap = (HashMap<String, Object>) map.get("xattr");

                    List<String> signers = (ArrayList<String>) xattrMap.get("signers");
                    String hash = (String) xattrMap.get("hash");
                    int pages = (int) xattrMap.get("pages");

                    Map<String, String> uriMap = (HashMap<String, String>) map.get("uri");
                    String path = uriMap.get("path");
                    String merkleroot = uriMap.get("hash");

                    assertThat(owner.equals(addr));
                    if (owner.equals(addr)) {
                        logger.info("query owner true");
                    } else {
                        logger.info("query owner fail");
                    }


                    assertThat(type.equals(this.type));
                    if (type.equals(this.type)) {
                        logger.info("query type true");
                    } else {
                        logger.info("query type fail");
                    }

                    assertThat(signers.get(0).equals(this.signers));
                    if (signers.get(0).equals(this.signers)) {
                        logger.info("query signers true");
                    } else {
                        logger.info("query signers fail");
                    }

                    assertThat(hash.equals(this.hash));
                    if (hash.equals(this.hash)) {
                        logger.info("query hash true");
                    } else {
                        logger.info("query hash fail");
                    }

                    assertThat(pages == this.pages);
                    if (pages == this.pages) {
                        logger.info("query page true");
                    } else {
                        logger.info("query page fail");
                    }

                    assertThat(path.equals(this.path));
                    if (path.equals(this.path)) {
                        logger.info("query path true");
                    } else {
                        logger.info("query path fail");
                    }

                    assertThat(merkleroot.equals(this.pathHash));
                    if (merkleroot.equals(this.pathHash)) {
                        logger.info("query pathHash true");
                    } else {
                        logger.info("query pathHash fail");
                    }


                } else {
                    logger.info("query fail");
                }
            }
        }
    }
}
