package com.poscoict.posledger.assets.chaincode;

import org.springframework.stereotype.Component;

@Component
public class ChaincodeRequest {

    private String functionName;
    private String chaincodeName;
    private String[] args;

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getChaincodeName() {
        return chaincodeName;
    }

    public void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

}
