package com.github.fabasset.controller;

import com.github.fabasset.chaincode.ChaincodeProxy;
import com.github.fabasset.chaincode.function.TokenTypeManagement;
import com.github.fabasset.config.ExecutionConfig;
import com.github.fabasset.config.NetworkConfig;
import com.github.fabasset.model.dao.UserDao;
import com.github.fabasset.util.RedisEnrollment;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
    String IP = "localhost";

    public void setOrg0() {
        NetworkConfig.ORG_MSP = "Org0MSP";
        NetworkConfig.ORG = "org0";
        NetworkConfig.CA_ORG_URL = "http://" + IP + ":7054";
        NetworkConfig.ORG_PEER = "peer0.org0.example.com";
        NetworkConfig.ORG_PEER_URL = "grpc://" + IP + ":7051";
        NetworkConfig.ADMIN = "admin";
    }

    @GetMapping("/admin")
    public String admin() {
        log.info("admin page");

        return "admin";
    }

    @PostMapping("/enrollTokenType")
    public String enrollTokenType(HttpServletRequest req) throws Exception {

        log.info("enrollTokenType ####################");
        setOrg0();

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
                xattrValue = "false";
            else
                return "FAILURE";

            xattr.put(xattrName, new ArrayList<>(Arrays.asList(xattrType, xattrValue)));
        }

        Enrollment enrollment = re.getEnrollment(ownerKey);
        ChaincodeProxy chaincodeProxy = ExecutionConfig.initChaincodeProxy(ownerKey, enrollment);
        tokenTypeManagement.setChaincodeProxyAndChaincodeName(chaincodeProxy, chaincodeId);

        boolean result = tokenTypeManagement.enrollTokenType(tokenType, xattr);

        return "/admin";
    }

    @GetMapping("/adminTokenTypesOf")
    public String adminTokenTypesOf(HttpServletRequest req) throws InvalidArgumentException, ProposalException {

        return "/adminTokenTypesOf";
    }

    @ResponseBody
    @RequestMapping("/tokenTypesOf")
    public String tokenTypesOf(HttpServletRequest req) throws Exception {

        String ownerKey = req.getParameter("ownerKey");
        String result = "";
        setOrg0();

        Enrollment enrollment = re.getEnrollment(ownerKey);
        ChaincodeProxy chaincodeProxy = ExecutionConfig.initChaincodeProxy(ownerKey, enrollment);
        tokenTypeManagement.setChaincodeProxyAndChaincodeName(chaincodeProxy, chaincodeId);

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
    public String retrieveTokenType(HttpServletRequest req) throws Exception {

        String tokenType = req.getParameter("tokenType");
        String ownerKey = req.getParameter("ownerKey");

        setOrg0();
        log.info("###########################" + ownerKey);

        Enrollment enrollment = re.getEnrollment(ownerKey);
        ChaincodeProxy chaincodeProxy = ExecutionConfig.initChaincodeProxy(ownerKey, enrollment);
        tokenTypeManagement.setChaincodeProxyAndChaincodeName(chaincodeProxy, chaincodeId);

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
    public String retrieveAttributeOfTokenType(HttpServletRequest req) throws Exception {

        String tokenType = req.getParameter("tokenType");
        String ownerKey = req.getParameter("ownerKey");
        String attribute = (req.getParameter("xattrName"));
        String result = "";

        setOrg0();
        log.info("retrieveAttributeOfTokenType ####################");

        Enrollment enrollment = re.getEnrollment(ownerKey);
        ChaincodeProxy chaincodeProxy = ExecutionConfig.initChaincodeProxy(ownerKey, enrollment);
        tokenTypeManagement.setChaincodeProxyAndChaincodeName(chaincodeProxy, chaincodeId);

        List<String> pair = tokenTypeManagement.retrieveAttributeOfTokenType(tokenType, attribute);
        if(pair != null) {
            result += "xattrType : " + pair.get(0) + "\n";
            result += "value : " + pair.get(0) + "\n";

        }

        return result;
    }


    @GetMapping("/adminDropTokenType")
    public String adminDropTokenType(HttpServletRequest req) throws InvalidArgumentException, ProposalException {

        return "/adminDropTokenType";
    }

    @ResponseBody
    @RequestMapping("/dropTokenType")
    public String dropTokenType(HttpServletRequest req) throws Exception {

        log.info("dropTokenType ####################");
        setOrg0();

        String tokenType = req.getParameter("tokenType");
        String ownerKey = req.getParameter("ownerKey");

        Enrollment enrollment = re.getEnrollment(ownerKey);
        ChaincodeProxy chaincodeProxy = ExecutionConfig.initChaincodeProxy(ownerKey, enrollment);
        tokenTypeManagement.setChaincodeProxyAndChaincodeName(chaincodeProxy, chaincodeId);

        boolean result = tokenTypeManagement.dropTokenType(tokenType);
        if(result == true)
            return "true";
        else
            return "false";

    }
}
