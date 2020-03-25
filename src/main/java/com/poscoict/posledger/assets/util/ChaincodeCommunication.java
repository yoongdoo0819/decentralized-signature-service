package com.poscoict.posledger.assets.util;

import com.poscoict.posledger.assets.client.ChannelClient;
import com.poscoict.posledger.assets.client.FabricClient;
import com.poscoict.posledger.assets.config.SetConfig;
import com.poscoict.posledger.assets.config.Config;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class ChaincodeCommunication {
    private static String chaincodeId = Manager.getChaincodeId();

    private ChaincodeCommunication() {}

    public static boolean sendTransaction(String function, String[] args) throws InvalidArgumentException, TransactionException, ProposalException, IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, CryptoException {
        boolean result = false;

        FabricClient fabClient = SetConfig.getFabClient();
        TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
        ChaincodeID ccid = ChaincodeID.newBuilder().setName(chaincodeId).build();
        request.setChaincodeID(ccid);
        request.setFcn(function);
        request.setArgs(args);

        ChannelClient channelClient = SetConfig.initChannel(Config.peerName, Config.peerURL, Config.ordererName, Config.ordererURL, Config.eventHubName, Config.eventHubURL);
        Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
        for (ProposalResponse response : responses) {
            result = Boolean.parseBoolean(response.getMessage());
        }

        return result;
    }

    public static String queryByChainCode(String function, String[] args) throws InvalidArgumentException, TransactionException, ProposalException {
        String result = null;

        ChannelClient channelClient = SetConfig.initChannel(Config.peerName, Config.peerURL, Config.ordererName, Config.ordererURL, Config.eventHubName, Config.eventHubURL);
        Collection<ProposalResponse> responses = channelClient.queryByChainCode(chaincodeId, function, args);
        for (ProposalResponse response : responses) {
            result = response.getMessage();
        }

        return result;
    }
}
