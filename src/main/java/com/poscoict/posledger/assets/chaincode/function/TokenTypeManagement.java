package com.poscoict.posledger.assets.chaincode.function;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscoict.posledger.assets.chaincode.ChaincodeProxy;
import com.poscoict.posledger.assets.chaincode.InvokeChaincode;
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

import static com.poscoict.posledger.assets.util.Function.*;

@Component
public class TokenTypeManagement {
    private static final Logger logger = LogManager.getLogger(TokenTypeManagement.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    private InvokeChaincode invokeChaincode = InvokeChaincode.getInstance();

    private ChaincodeProxy chaincodeProxy;

    private String chaincodeName;


    public List<String> tokenTypesOf() throws ProposalException, InvalidArgumentException {
        logger.info("---------------- tokenTypesOf SDK called ----------------");

        String[] args = {};
        String result = invokeChaincode.queryByChainCode(chaincodeProxy, chaincodeName, TOKEN_TYPES_OF_FUNCTION_NAME, args);

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
        String result = invokeChaincode.submitTransaction(chaincodeProxy, chaincodeName, ENROLL_TOKEN_TYPE_FUNCTION_NAME, args);

        if (result == null) {
            throw new NullPointerException("Invalid result");
        }

        return Boolean.parseBoolean(result);
    }

    public boolean dropTokenType(String type) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- dropTokenType SDK called ----------------");

        String[] args = { type };
        String result = invokeChaincode.submitTransaction(chaincodeProxy, chaincodeName, DROP_TOKEN_TYPE_FUNCTION_NAME, args);

        if (result == null) {
            throw new NullPointerException("Invalid result");
        }

        return Boolean.parseBoolean(result);
    }

    public Map<String, List<String>> retrieveTokenType(String type) throws ProposalException, InvalidArgumentException, IOException {
        logger.info("---------------- getTokenType SDK called ----------------");

        String[] args = { type };
        String result = invokeChaincode.queryByChainCode(chaincodeProxy, chaincodeName, RETRIEVE_TOKEN_TYPE_FUNCTION_NAME, args);

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
        String result = invokeChaincode.queryByChainCode(chaincodeProxy, chaincodeName, RETRIEVE_ATTRIBUTE_OF_TOKEN_TYPE_FUNCTION_NAME, args);
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
/*
@Component
public class TokenTypeManagement extends SDK {
    private static final Logger logger = LogManager.getLogger(TokenTypeManagement.class);

    private ObjectMapper objectMapper;

    public TokenTypeManagement() {
        super();
        this.objectMapper = new ObjectMapper();
    }

    public TokenTypeManagement(ObjectMapper objectMapper) {
        super(objectMapper);
        this.objectMapper = super.getObjectMapper();
    }

    public List<String> tokenTypesOf() throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- tokenTypesOf SDK called ----------------");

        String tokenTypsString;
        List<String> tokenTypes = new ArrayList<String>();
        try {
            String[] args = {};
            tokenTypsString = ChaincodeCommunication.queryByChainCode(TOKEN_TYPES_OF_FUNCTION_NAME, args);
            if (tokenTypsString != null) {
                tokenTypes = Arrays.asList(tokenTypsString.substring(1, tokenTypsString.length() - 1).split(", "));
            }
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return tokenTypes;
    }

    public boolean enrollTokenType(String type, Map<String, List<String>> xattr) throws ProposalException, InvalidArgumentException, JsonProcessingException, TransactionException, Exception {
        logger.info("---------------- enrollTokenType SDK called ----------------");

        boolean result = false;
        try {
            String json = objectMapper.writeValueAsString(xattr);
            String[] args = { type, json };
            result = ChaincodeCommunication.sendTransaction(ENROLL_TOKEN_TYPE_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean dropTokenType(String type) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- dropTokenType SDK called ----------------");

        boolean result;
        try {
            String[] args = { type };
            result = ChaincodeCommunication.sendTransaction(DROP_TOKEN_TYPE_FUNCTION_NAME, args);
        } catch (ProposalException | IllegalAccessException | InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | CryptoException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public Map<String, List<String>> retrieveTokenType(String type) throws ProposalException, InvalidArgumentException, IOException, TransactionException {
        logger.info("---------------- getTokenType SDK called ----------------");

        String json;
        Map<String, List<String>> xattr = new HashMap<String, List<String>>();
        try {
            String[] args = { type };
            json = ChaincodeCommunication.queryByChainCode(RETRIEVE_TOKEN_TYPE_FUNCTION_NAME, args);
            if (json != null) {
                xattr = objectMapper.readValue(json, new TypeReference<Map<String, List<String>>>() {});
            }
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return xattr;
    }

    public List<String> retrieveAttributeOfTokenType(String type, String attribute) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- retrieveAttributeOfTokenType SDK called ----------------");

        String string;
        List<String> pair = new ArrayList<String>();
        try {
            String[] args = { type, attribute };
            string = ChaincodeCommunication.queryByChainCode(RETRIEVE_ATTRIBUTE_OF_TOKEN_TYPE_FUNCTION_NAME, args);
            if (string != null) {
                pair = Arrays.asList(string.substring(1, string.length() - 1).split(", "));
            }
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return pair;
    }
}

 */
