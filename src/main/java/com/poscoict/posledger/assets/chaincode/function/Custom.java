package com.poscoict.posledger.assets.chaincode.function;

import com.poscoict.posledger.assets.chaincode.ChaincodeProxy;
import com.poscoict.posledger.assets.chaincode.InvokeChaincode;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.stereotype.Component;

import static com.poscoict.posledger.assets.util.Function.SIGN_FUNCTION_NAME;

@Component
public class Custom {

    private static final Logger logger = LogManager.getLogger(ERC721.class);

    private InvokeChaincode invokeChaincode = InvokeChaincode.getInstance();

    private ChaincodeProxy chaincodeProxy;

    private String chaincodeName;

    public void setChaincodeProxyAndChaincodeName(ChaincodeProxy chaincodeProxy, String chaincodeName) {
        this.chaincodeProxy = chaincodeProxy;
        this.chaincodeName = chaincodeName;
    }

    public boolean sign(String tokenId, String signature) throws ProposalException, InvalidArgumentException, TransactionException {
        logger.info("---------------- sign SDK called ----------------");

        String result;

        String[] args = { tokenId, signature };
        result = invokeChaincode.submitTransaction(chaincodeProxy, chaincodeName, SIGN_FUNCTION_NAME, args);

        if(result == null)
            throw new NullPointerException("Invalid result");

        return Boolean.parseBoolean(result);
    }
}