package com.github.fabasset.example.chaincode.function;

import com.github.fabasset.example.util.AddressUtils;
import com.github.fabasset.example.chaincode.ChaincodeProxy;
import com.github.fabasset.example.chaincode.InvokeChaincode;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.stereotype.Component;

import static com.github.fabasset.example.util.Function.*;

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
