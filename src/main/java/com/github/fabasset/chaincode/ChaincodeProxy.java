package com.github.fabasset.chaincode;

import com.github.fabasset.client.ChannelClient;
import com.github.fabasset.client.FabricClient;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ChaincodeProxy {

    private FabricClient fabricClient;
    private ChannelClient channelClient;

    public void setFabricClient(FabricClient fabricClient) {
        this.fabricClient = fabricClient;
    }

    public void setChannelClient(ChannelClient channelClient) {
        this.channelClient = channelClient;
    }

    Collection<ProposalResponse> submitTransaction(ChaincodeRequest chaincodeRequest) throws ProposalException, InvalidArgumentException {

        TransactionProposalRequest request = fabricClient.getInstance().newTransactionProposalRequest();
        ChaincodeID ccid = ChaincodeID.newBuilder().setName(chaincodeRequest.getChaincodeName()).build();
        request.setChaincodeID(ccid);
        request.setFcn(chaincodeRequest.getFunctionName());
        request.setArgs(chaincodeRequest.getArgs());

        return channelClient.sendTransactionProposal(request);
    }

    Collection<ProposalResponse> queryByChainCode(ChaincodeRequest chaincodeRequest) throws ProposalException, InvalidArgumentException {
        return channelClient.queryByChainCode(chaincodeRequest.getChaincodeName(), chaincodeRequest.getFunctionName(), chaincodeRequest.getArgs());
    }
}
