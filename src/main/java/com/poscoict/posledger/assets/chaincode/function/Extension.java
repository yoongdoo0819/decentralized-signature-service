package com.poscoict.posledger.assets.chaincode.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscoict.posledger.assets.chaincode.AddressUtils;
import com.poscoict.posledger.assets.chaincode.ChaincodeProxy;
import com.poscoict.posledger.assets.chaincode.InvokeChaincode;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.poscoict.posledger.assets.util.Function.*;

@Component
public class Extension {
    private static final Logger logger = LogManager.getLogger(Extension.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    private InvokeChaincode invokeChaincode = InvokeChaincode.getInstance();

    private ChaincodeProxy chaincodeProxy;

    private String chaincodeName;

    public void setChaincodeProxyAndChaincodeName(ChaincodeProxy chaincodeProxy, String chaincodeName) {
        this.chaincodeProxy = chaincodeProxy;
        this.chaincodeName = chaincodeName;
    }

    public long balanceOf(String owner, String type) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- balanceOf SDK called ----------------");

        if (!AddressUtils.isValidAddress(owner)) {
            throw new IllegalArgumentException();
        }

        String[] args = { owner, type };
        String result = invokeChaincode.queryByChainCode(chaincodeProxy, chaincodeName, BALANCE_OF_FUNCTION_NAME, args);

        if (result == null) {
            throw new NullPointerException("Invalid result");
        }

        return Long.parseLong(result);
    }

    public List<String> tokenIdsOf(String owner, String type) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- tokenIdsOf SDK called ----------------");

        if (!AddressUtils.isValidAddress(owner)) {
            throw new IllegalArgumentException();
        }

        String[] args = { owner, type };
        String result = invokeChaincode.queryByChainCode(chaincodeProxy, chaincodeName, TOKEN_IDS_OF_FUNCTION_NAME, args);

        List<String> tokenIds = null;
        if(result != null) {
            tokenIds = Arrays.asList(result.substring(1, result.length() - 1).split(", "));
        }

        if (tokenIds == null) {
            throw new NullPointerException("Invalid result");
        }

        return tokenIds;
    }

    public boolean mint(String tokenId, String type, Map<String, Object> xattr, Map<String, String> uri) throws ProposalException, InvalidArgumentException, JsonProcessingException, JsonProcessingException {
        logger.info("---------------- mint SDK called ----------------");

        String xattrJson = objectMapper.writeValueAsString(xattr);
        String uriJson = objectMapper.writeValueAsString(uri);
        String[] args = { tokenId, type, xattrJson, uriJson };
        String result = invokeChaincode.submitTransaction(chaincodeProxy, chaincodeName, MINT_FUNCTION_NAME, args);

        if (result == null) {
            throw new NullPointerException("Invalid result");
        }

        return Boolean.parseBoolean(result);
    }

    public boolean setURI(String tokenId, String index, String value) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- setURI SDK called ----------------");

        String[] args = { tokenId, index, value };
        String result = invokeChaincode.submitTransaction(chaincodeProxy, chaincodeName, SET_URI_FUNCTION_NAME, args);

        if (result == null) {
            throw new NullPointerException("Invalid result");
        }

        return Boolean.parseBoolean(result);
    }

    public String getURI(String tokenId, String index) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- getURI SDK called ----------------");

        String[] args = { tokenId, index };
        String result = invokeChaincode.queryByChainCode(chaincodeProxy, chaincodeName, GET_URI_FUNCTION_NAME, args);

        if (result == null) {
            throw new NullPointerException("Invalid result");
        }

        return result;
    }

    public boolean setXAttr(String tokenId, String index, Object value) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- setXAttr SDK called ----------------");

        String[] args = { tokenId, index, String.valueOf(value) };
        String result = invokeChaincode.submitTransaction(chaincodeProxy, chaincodeName, SET_XATTR_FUNCTION_NAME, args);

        if (result == null) {
            throw new NullPointerException("Invalid result");
        }

        return Boolean.parseBoolean(result);
    }

    public String getXAttr(String tokenId, String index) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- getXAttr SDK called ----------------");

        String[] args = { tokenId, index };
        String result = invokeChaincode.queryByChainCode(chaincodeProxy, chaincodeName, GET_XATTR_FUNCTION_NAME, args);

        if (result == null) {
            throw new NullPointerException("Invalid result");
        }

        return result;
    }
}

/*
@Component
public class Extension extends SDK {

    private static final Logger logger = LogManager.getLogger(Extension.class);

    private ObjectMapper objectMapper;

    public Extension() {
        super();
        this.objectMapper = new ObjectMapper();
    }

    public Extension(ObjectMapper objectMapper) {
        super(objectMapper);
        this.objectMapper = super.getObjectMapper();
    }

    public boolean mint(String tokenId, String type, Map<String, Object> xattr, Map<String, String> uri) throws Exception {
        logger.info("---------------- mint SDK called ----------------");

        boolean result = false;
        try {
            String xattrJson = objectMapper.writeValueAsString(xattr);
            String uriJson = objectMapper.writeValueAsString(uri);
            String[] args = { tokenId, type, xattrJson, uriJson };
            result = ChaincodeCommunication.sendTransaction(MINT_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean setURI(String tokenId, String index, String value) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- setURI SDK called ----------------");

        boolean result;
        try {
            String[] args = { tokenId, index, value };
            result = ChaincodeCommunication.sendTransaction(SET_URI_FUNCTION_NAME, args);
        } catch (ProposalException | IllegalAccessException | InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | CryptoException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public String getURI(String tokenId, String index) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- getURI SDK called ----------------");

        String value;
        try {
            String[] args = { tokenId, index };
            value = ChaincodeCommunication.queryByChainCode(GET_URI_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return value;
    }

    public boolean setXAttr(String tokenId, String index, Object value) throws ProposalException, InvalidArgumentException, TransactionException, Exception {
        logger.info("---------------- setXAttr SDK called ----------------");

        boolean result = false;
        try {
            String[] args = { tokenId, index, String.valueOf(value) };
            result = ChaincodeCommunication.sendTransaction(SET_XATTR_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public String getXAttr(String tokenId, String index) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- getXAttr SDK called ----------------");

        String value;
        try {
            String[] args = { tokenId, index };
            value = ChaincodeCommunication.queryByChainCode(GET_XATTR_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return value;
    }

    public long balanceOf(String owner, String type) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- balanceOf SDK called ----------------");

        long balance;
        try {
            String[] args = { owner, type };
            String balanceStr = ChaincodeCommunication.queryByChainCode(BALANCE_OF_FUNCTION_NAME, args);
            balance = Long.parseLong(balanceStr);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return balance;
    }

    public List<String> tokenIdsOf(String owner, String type) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- tokenIdsOf SDK called ----------------");

        List<String> tokenIds = new ArrayList<String>();
        try {
            String[] args = { owner, type };
            String tokenIdsStr = ChaincodeCommunication.queryByChainCode(TOKEN_IDS_OF_FUNCTION_NAME, args);

            if(tokenIdsStr != null) {
                tokenIds = Arrays.asList(tokenIdsStr.substring(1, tokenIdsStr.length() - 1).split(", "));
            }
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return tokenIds;
    }

}

 */
