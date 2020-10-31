package com.github.fabasset.example.chaincode;

import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class InvokeChaincode {

    private static InvokeChaincode instance = new InvokeChaincode();

    private InvokeChaincode() {}

    public static InvokeChaincode getInstance() {
        return instance;
    }

    public String submitTransaction(ChaincodeProxy chaincodeProxy, String chaincodeId, String functionName, String[] args) throws InvalidArgumentException, ProposalException {
        String result = null;

        ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
        chaincodeRequest.setChaincodeName(chaincodeId);
        chaincodeRequest.setFunctionName(functionName);
        chaincodeRequest.setArgs(args);

        Collection<ProposalResponse> responses = chaincodeProxy.submitTransaction(chaincodeRequest);
        for (ProposalResponse response : responses) {
            result = response.getMessage();
        }

        return result;
    }

    public String queryByChainCode(ChaincodeProxy chaincodeProxy, String chaincodeId, String functionName, String[] args) throws InvalidArgumentException, ProposalException {
        String result = null;

        ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
        chaincodeRequest.setChaincodeName(chaincodeId);
        chaincodeRequest.setFunctionName(functionName);
        chaincodeRequest.setArgs(args);

        Collection<ProposalResponse> responses = chaincodeProxy.queryByChainCode(chaincodeRequest);
        for (ProposalResponse response : responses) {
            result = response.getMessage();
        }

        return result;
    }
}
