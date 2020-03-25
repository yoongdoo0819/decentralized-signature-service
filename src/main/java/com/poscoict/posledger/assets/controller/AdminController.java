package com.poscoict.posledger.assets.controller;

import com.poscoict.posledger.assets.chaincode.AddressUtils;
import com.poscoict.posledger.assets.chaincode.EnrollmentUser;
import com.poscoict.posledger.assets.chaincode.RedisEnrollment;
import com.poscoict.posledger.assets.chaincode.TokenTypeManagement;
import com.poscoict.posledger.assets.config.Config;
import com.poscoict.posledger.assets.config.SetConfig;
import com.poscoict.posledger.assets.model.User;
import com.poscoict.posledger.assets.model.dao.UserDao;
import com.poscoict.posledger.assets.user.UserContext;
import com.poscoict.posledger.assets.util.Manager;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.identity.X509Identity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

import static java.lang.Integer.parseInt;

@Slf4j
@Controller
public class AdminController {

    @Autowired
    private TokenTypeManagement tokenTypeManagement;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisEnrollment re;

    private String chaincodeId = "mycc";

    @GetMapping("/adminSignUpForm")
    public String adminSignUpForm() {
        log.info("adminSignUpForm form");

        return "adminSignUpForm";
    }

    @PostMapping("/adminSignUp")
    public String adminSignUp(HttpServletRequest req) {
        log.info("adminSignUp");
        String userId = req.getParameter("userId");
        String passwd = req.getParameter("userPasswd");

        EnrollmentUser newUser = new EnrollmentUser();
        try {

            Enrollment enrollment = newUser.enrollAdmin(userId, passwd);

            UserContext userContext = new UserContext();
            userContext.setName(userId);
            userContext.setAffiliation("org1.department1");
            userContext.setMspId("Org1MSP");
            userContext.setEnrollment(enrollment);
            X509Identity identity = new X509Identity(userContext);

            AddressUtils addressUtils = new AddressUtils();
            String addr = addressUtils.getMyAddress(identity);
            System.out.println(addr);

            // insert user's cert into Redis
            if(!(re.setEnrollment(userId, enrollment)))
                log.info("user register failure");

            // insert user's info into DB
            User user = new User(userId, addr, passwd, req.getParameter("userEmail"));
            userDao.insert(user);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "index";
    }

    @GetMapping("/admin")
    public String admin() {
        log.info("admin page");

        return "admin";
    }

    @GetMapping("/addConfig")
    public String addConfig() {
        log.info("addConfig page");

        return "addConfig";
    }

    @PostMapping("/setConfig")
    public String setConfig(HttpServletRequest req) {
        log.info("setConfig");
        Config.CA_ORG1_URL = req.getParameter("caURL");
        Config.CHANNEL_NAME = req.getParameter("channelName");
        Config.CHAINCODE_1_NAME = req.getParameter("chaincodeName");

        log.info(Config.CA_ORG1_URL);
        log.info(Config.CHANNEL_NAME);
        log.info(Config.CHAINCODE_1_NAME);
        return "index";
    }

    @GetMapping("/addPeer")
    public String addPeer() {
        log.info("addPeer page");

        return "addPeer";
    }

    @PostMapping("/setPeer")
    public String setPeer(HttpServletRequest req) {
        log.info("setPeer");
        String count = req.getParameter("count");

        if(Config.peerName == null) {
            Config.peerName = new ArrayList<>();
            Config.peerURL = new ArrayList<>();
        }

        for(int i=0; i<Integer.valueOf(count); i++) {

            Config.peerName.add(req.getParameter("peerName"+i));
            Config.peerURL.add(req.getParameter("peerURL"+i));
            log.info(Config.peerName.get(i) + " / " + Config.peerURL.get(i));

        }

        return "addPeer";
    }

    @GetMapping("/addOrderer")
    public String addOrderer() {
        log.info("addOrderer page");

        return "addOrderer";
    }

    @PostMapping("/setOrderer")
    public String setOrderer(HttpServletRequest req) {
        log.info("setOrderer");
        String count = req.getParameter("count");

        if(Config.ordererName == null) {
            Config.ordererName = new ArrayList<>();
            Config.ordererURL = new ArrayList<>();
        }

        for(int i=0; i<Integer.valueOf(count); i++) {

            Config.ordererName.add(req.getParameter("ordererName"+i));
            Config.ordererURL.add(req.getParameter("ordererURL"+i));
            log.info(Config.ordererName.get(i) + " / " + Config.ordererURL.get(i));
        }

        return "addOrderer";
    }

    @GetMapping("/addEventHub")
    public String addEventHub() {
        log.info("addEventHub page");

        return "addEventHub";
    }

    @PostMapping("/setEventHub")
    public String setEventHub(HttpServletRequest req) {
        log.info("setEventHub");
        String count = req.getParameter("count");

        if(Config.eventHubName == null) {
            Config.eventHubName = new ArrayList<>();
            Config.eventHubURL = new ArrayList<>();
        }

        for(int i=0; i<Integer.valueOf(count); i++) {

            Config.eventHubName.add(req.getParameter("eventHubName"+i));
            Config.eventHubURL.add(req.getParameter("eventHubURL"+i));
            log.info(Config.eventHubName.get(i) + " / " + Config.eventHubURL.get(i));
        }

        return "addEventHub";
    }

    @PostMapping("/enrollTokenType")
    public String enrollTokenType(HttpServletRequest req) throws Exception {

        log.info("enrollTokenType ####################");

        String ownerKey = req.getParameter("ownerKey");
        String tokenType = req.getParameter("tokenType");
        int xattrCount = parseInt(req.getParameter("xattrCount"));
        //int uriCount = parseInt(req.getParameter("uriCount"));

        String xattrName = "";
        String xattrType = "";
        String xattrValue = "";

        Map<String, List<String>> xattr = new HashMap<>();

        for(int i=0; i<xattrCount; i++) {
            xattrName = req.getParameter("xattrName" + i);
            xattrType = req.getParameter("xattrType" + i);
            log.info(xattrName + " , " + xattrType);

            if (xattrType.equals("String"))
                xattrValue = "";
            else if (xattrType.equals("[String]"))
                xattrValue = "[]";
            else if (xattrType.equals("Integer"))
                xattrValue = "0";
            else if (xattrType.equals("Boolean"))
                xattrValue = "true";
            else
                return "FAILURE";

            xattr.put(xattrName, new ArrayList<>(Arrays.asList(xattrType, xattrValue)));
        }

        Enrollment enrollment = re.getEnrollment(ownerKey);
        SetConfig.initUserContext(ownerKey, enrollment);
        Manager.setChaincodeId(chaincodeId);

        boolean result = tokenTypeManagement.enrollTokenType(tokenType, xattr);

        return "/admin";
    }

    @GetMapping("/adminTokenTypesOf")
    public String adminTokenTypesOf(HttpServletRequest req) throws InvalidArgumentException, ProposalException {

        return "/adminTokenTypesOf";
    }

    @ResponseBody
    @RequestMapping("/tokenTypesOf")
    public String tokenTypesOf(HttpServletRequest req) throws InvalidArgumentException, ProposalException, TransactionException {

        String result = "";

        Manager.setChaincodeId(chaincodeId);
        List<String> types = tokenTypeManagement.tokenTypesOf();

        for(int i=0; i<types.size(); i++) {
            result += "type " + i + " : " + types.get(i) + "\n";
        }

        log.info("tokenTypesOf ####################");
        return result;
    }

    @GetMapping("/adminRetrieveTokenType")
    public String adminRetrieveTokenType(HttpServletRequest req) throws InvalidArgumentException, ProposalException {

        return "/adminRetrieveTokenType";
    }

    @ResponseBody
    @RequestMapping("/retrieveTokenType")
    public String retrieveTokenType(HttpServletRequest req) throws ProposalException, InvalidArgumentException, IOException, TransactionException {

        String tokenType = req.getParameter("tokenType");
        String ownerKey = req.getParameter("ownerKey");

        Manager.setChaincodeId(chaincodeId);

        Map<String, List<String>> attributes = tokenTypeManagement.retrieveTokenType(tokenType);

        log.info(attributes.toString());
        List<String> keys = new ArrayList<>(attributes.keySet());

        String result = "";
        for(int i = 0; i<keys.size(); i++) {
            result += "Attributes" + i + " : " + keys.get(i) + "\n";
        }

        log.info("retrieveTokenType ####################");
        return result;
    }



    @GetMapping("/adminRetrieveAttributeOfTokenType")
    public String adminRetrieveAttributeOfTokenType(HttpServletRequest req) throws InvalidArgumentException, ProposalException {

        return "/adminRetrieveAttributeOfTokenType";
    }

    @ResponseBody
    @RequestMapping("/retrieveAttributeOfTokenType")
    public String retrieveAttributeOfTokenType(HttpServletRequest req) throws InvalidArgumentException, ProposalException, TransactionException {

        String tokenType = req.getParameter("tokenType");
        String ownerKey = req.getParameter("ownerKey");
        String attribute = (req.getParameter("xattrName"));
        String result = "";

        log.info("retrieveAttributeOfTokenType ####################");

        Manager.setChaincodeId(chaincodeId);

        List<String> pair = tokenTypeManagement.retrieveAttributeOfTokenType(tokenType, attribute);
        if(pair != null) {
            result += "xattrType : " + pair.get(0) + "\n";
            result += "value : " + pair.get(1) + "\n";

        }

        return result;
    }


    @GetMapping("/adminDropTokenType")
    public String adminDropTokenType(HttpServletRequest req) throws InvalidArgumentException, ProposalException {

        return "/adminDropTokenType";
    }

    @ResponseBody
    @RequestMapping("/dropTokenType")
    public String dropTokenType(HttpServletRequest req) throws InvalidArgumentException, ProposalException, TransactionException {

        log.info("dropTokenType ####################");

        String tokenType = req.getParameter("tokenType");
        String ownerKey = req.getParameter("ownerKey");

        Manager.setChaincodeId(chaincodeId);

        boolean result = tokenTypeManagement.dropTokenType(tokenType);
        if(result == true)
            return "true";
        else
            return "false";

    }
}
