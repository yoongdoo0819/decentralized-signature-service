package com.github.fabasset.example.chaincode.function;


import com.github.fabasset.sdk.chaincode.ChaincodeProxy;
import com.github.fabasset.sdk.chaincode.InvokeChaincode;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.stereotype.Component;



@Component
public class Custom {

    private static final Logger logger = LogManager.getLogger(Custom.class);

    private InvokeChaincode invokeChaincode = InvokeChaincode.getInstance();
    private ChaincodeProxy chaincodeProxy;
    private String chaincodeName;
    private static final String SIGN_FUNCTION_NAME = "sign";
    private static final String FINALIZE_FUNCTION_NAME = "finalize";

    public void setChaincodeProxy(ChaincodeProxy chaincodeProxy) {
        this.chaincodeProxy = chaincodeProxy;
    }

    public void setChaincodeName(String chaincodeName) {
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
