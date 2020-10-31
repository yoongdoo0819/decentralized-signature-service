package com.github.fabasset.chaincode.function;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fabasset.chaincode.InvokeChaincode;
import com.github.fabasset.util.Function;
import com.github.fabasset.chaincode.ChaincodeProxy;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class TokenTypeManagement {
    private static final Logger logger = LogManager.getLogger(TokenTypeManagement.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    private InvokeChaincode invokeChaincode = InvokeChaincode.getInstance();

    private ChaincodeProxy chaincodeProxy;

    private String chaincodeName;

    public void setChaincodeProxyAndChaincodeName(ChaincodeProxy chaincodeProxy, String chaincodeName) {
        this.chaincodeProxy = chaincodeProxy;
        this.chaincodeName = chaincodeName;
    }

    public List<String> tokenTypesOf() throws ProposalException, InvalidArgumentException {
        logger.info("---------------- tokenTypesOf SDK called ----------------");

        String[] args = {};
        String result = invokeChaincode.queryByChainCode(chaincodeProxy, chaincodeName, Function.TOKEN_TYPES_OF_FUNCTION_NAME, args);

        List<String> tokenTypes = null;
        if (result != null) {
            tokenTypes = Arrays.asList(result.substring(1, result.length() - 1).split(", "));
        }

        if (tokenTypes == null) {
            throw new NullPointerException("Invalid result");
        }

        return tokenTypes;
    }

    public boolean enrollTokenType(String type, Map<String, List<String>> xattr) throws ProposalException, InvalidArgumentException, JsonProcessingException, TransactionException {
        logger.info("---------------- enrollTokenType SDK called ----------------");

        String json = objectMapper.writeValueAsString(xattr);
        String[] args = { type, json };
        String result = invokeChaincode.submitTransaction(chaincodeProxy, chaincodeName, Function.ENROLL_TOKEN_TYPE_FUNCTION_NAME, args);

        if (result == null) {
            throw new NullPointerException("Invalid result");
        }

        return Boolean.parseBoolean(result);
    }

    public boolean dropTokenType(String type) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- dropTokenType SDK called ----------------");

        String[] args = { type };
        String result = invokeChaincode.submitTransaction(chaincodeProxy, chaincodeName, Function.DROP_TOKEN_TYPE_FUNCTION_NAME, args);

        if (result == null) {
            throw new NullPointerException("Invalid result");
        }

        return Boolean.parseBoolean(result);
    }

    public Map<String, List<String>> retrieveTokenType(String type) throws ProposalException, InvalidArgumentException, IOException {
        logger.info("---------------- getTokenType SDK called ----------------");

        String[] args = { type };
        String result = invokeChaincode.queryByChainCode(chaincodeProxy, chaincodeName, Function.RETRIEVE_TOKEN_TYPE_FUNCTION_NAME, args);

        Map<String, List<String>> xattr = null;
        if (result != null) {
            xattr = objectMapper.readValue(result, new TypeReference<Map<String, List<String>>>() {});
        }

        if (xattr == null) {
            throw new NullPointerException("Invalid result");
        }

        return xattr;
    }

    public List<String> retrieveAttributeOfTokenType(String type, String attribute) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- retrieveAttributeOfTokenType SDK called ----------------");

        String[] args = { type, attribute };
        String result = invokeChaincode.queryByChainCode(chaincodeProxy, chaincodeName, Function.RETRIEVE_ATTRIBUTE_OF_TOKEN_TYPE_FUNCTION_NAME, args);
        List<String> pair = null;
        if (result != null) {
            pair = Arrays.asList(result.substring(1, result.length() - 1).split(", "));
        }

        if (pair == null) {
            throw new NullPointerException("Invalid result");
        }

        return pair;
    }
}

