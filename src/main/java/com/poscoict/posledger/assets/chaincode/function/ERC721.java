package com.poscoict.posledger.assets.chaincode.function;

import com.poscoict.posledger.assets.chaincode.ChaincodeProxy;
import com.poscoict.posledger.assets.chaincode.InvokeChaincode;
import com.poscoict.posledger.assets.util.AddressUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.stereotype.Component;

import static com.poscoict.posledger.assets.util.Function.*;

@Component
public class ERC721 {
    private static final Logger logger = LogManager.getLogger(ERC721.class);

    private InvokeChaincode invokeChaincode = InvokeChaincode.getInstance();

    private ChaincodeProxy chaincodeProxy;

    private String chaincodeName;

    public void setChaincodeProxyAndChaincodeName(ChaincodeProxy chaincodeProxy, String chaincodeName) {
        this.chaincodeProxy = chaincodeProxy;
        this.chaincodeName = chaincodeName;
    }

    public long balanceOf(String owner) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- balanceOf SDK called ----------------");

        if (!AddressUtils.isValidAddress(owner)) {
            throw new IllegalArgumentException();
        }

        String[] args = { owner };
        String result = invokeChaincode.queryByChainCode(chaincodeProxy, chaincodeName, BALANCE_OF_FUNCTION_NAME, args);

        if (result == null) {
            throw new NullPointerException("Invalid result");
        }

        return Long.parseLong(result);
    }

    public String ownerOf(String tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- ownerOf SDK called ----------------");

        String[] args = { tokenId };
        String owner = invokeChaincode.queryByChainCode(chaincodeProxy, chaincodeName, OWNER_OF_FUNCTION_NAME, args);

        if (owner == null) {
            throw new NullPointerException("Invalid result");
        }

        return owner;
    }

    public boolean transferFrom(String from, String to, String tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- transferFrom SDK called ----------------");

        if (!AddressUtils.isValidAddress(from) || !AddressUtils.isValidAddress(to)) {
            throw new IllegalArgumentException();
        }

        String[] args = { from, to, tokenId };
        String result = invokeChaincode.submitTransaction(chaincodeProxy, chaincodeName, TRANSFER_FROM_FUNCTION_NAME, args);

        if (result == null) {
            throw new NullPointerException("Invalid result");
        }

        return Boolean.parseBoolean(result);
    }

    public boolean approve(String approved, String tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- approve SDK called ----------------");

        if (!AddressUtils.isValidAddress(approved)) {
            throw new IllegalArgumentException();
        }

        String[] args = { approved, tokenId };
        String result = invokeChaincode.submitTransaction(chaincodeProxy, chaincodeName, APPROVE_FUNCTION_NAME, args);

        if (result == null) {
            throw new NullPointerException("Invalid result");
        }

        return Boolean.parseBoolean(result);
    }

    public boolean setApprovalForAll(String operator, boolean approved) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- setApprovalForAll SDK called ----------------");

        if (!AddressUtils.isValidAddress(operator)) {
            throw new IllegalArgumentException();
        }

        String[] args = { operator, Boolean.toString(approved) };
        String result = invokeChaincode.submitTransaction(chaincodeProxy, chaincodeName, SET_APPROVAL_FOR_ALL_FUNCTION_NAME, args);

        if (result == null) {
            throw new NullPointerException("Invalid result");
        }

        return Boolean.parseBoolean(result);
    }

    public String getApproved(String tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- getApproved SDK called ----------------");

        String[] args = { tokenId };
        String approved = invokeChaincode.queryByChainCode(chaincodeProxy, chaincodeName, GET_APPROVED_FUNCTION_NAME, args);

        if (approved == null) {
            throw new NullPointerException("Invalid result");
        }

        return approved;
    }

    public boolean isApprovedForAll(String owner, String operator) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- isApprovedForAll SDK called ----------------");

        if (!AddressUtils.isValidAddress(owner) || !AddressUtils.isValidAddress(operator)) {
            throw new IllegalArgumentException();
        }

        String[] args = { owner, operator };
        String result = invokeChaincode.queryByChainCode(chaincodeProxy, chaincodeName, IS_APPROVED_FOR_ALL_FUNCTION_NAME, args);

        if (result == null) {
            throw new NullPointerException("Invalid result");
        }

        return Boolean.parseBoolean(result);
    }

}
/*
@Component
public class ERC721 extends SDK {
    private static final Logger logger = LogManager.getLogger(ERC721.class);

    public ERC721() {
        super();
    }

    public ERC721(ObjectMapper objectMapper) {
        super(objectMapper);
    }



    public long balanceOf(String owner) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- balanceOf SDK called ----------------");

        long balance;
        try {
            String[] args = { owner };
            String balanceStr = ChaincodeCommunication.queryByChainCode(BALANCE_OF_FUNCTION_NAME, args);
            balance = Long.parseLong(balanceStr);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return balance;
    }

    public String ownerOf(String tokenId) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- ownerOf SDK called ----------------");

        String owner;
        try {
            String[] args = { tokenId };
            owner = ChaincodeCommunication.queryByChainCode(OWNER_OF_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return owner;
    }

    public boolean sign(String tokenId, String signature) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- sign SDK called ----------------");

        boolean result;
        try {
            String[] args = { tokenId, signature };
            result = ChaincodeCommunication.sendTransaction(SIGN_FUNCTION_NAME, args);
        } catch (ProposalException | IllegalAccessException | InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | CryptoException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean transferFrom(String from, String to, String tokenId) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- transferFrom SDK called ----------------");

        boolean result;
        try {
            String[] args = { from, to, tokenId };
            result = ChaincodeCommunication.sendTransaction(TRANSFER_FROM_FUNCTION_NAME, args);
        } catch (ProposalException | IllegalAccessException | InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | CryptoException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean approve(String approved, String tokenId) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- approve SDK called ----------------");

        boolean result = false;
        try {
            String[] args = { approved, tokenId };
            result = ChaincodeCommunication.sendTransaction(APPROVE_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (CryptoException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean setApprovalForAll(String operator, boolean approved) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- setApprovalForAll SDK called ----------------");

        boolean result;
        try {
            String[] args = { operator, Boolean.toString(approved) };
            result = ChaincodeCommunication.sendTransaction(SET_APPROVAL_FOR_ALL_FUNCTION_NAME, args);
        } catch (ProposalException | IllegalAccessException | InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | CryptoException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public String getApproved(String tokenId) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- getApproved SDK called ----------------");

        String approved;
        try {
            String[] args = { tokenId };
            approved = ChaincodeCommunication.queryByChainCode(GET_APPROVED_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return approved;
    }

    public boolean isApprovedForAll(String owner, String operator) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- isApprovedForAll SDK called ----------------");

        boolean result;
        try {
            String[] args = { owner, operator };
            String response = ChaincodeCommunication.queryByChainCode(IS_APPROVED_FOR_ALL_FUNCTION_NAME, args);
            result = Boolean.parseBoolean(response);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean finalize(String tokenId) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- finalize SDK called ----------------");

        boolean result;
        try {
            String[] args = { tokenId };
            result = ChaincodeCommunication.sendTransaction(FINALIZE_FUNCTION_NAME, args);
        } catch (ProposalException | IllegalAccessException | InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | CryptoException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public static void main(String[] args)  {}
}

 */