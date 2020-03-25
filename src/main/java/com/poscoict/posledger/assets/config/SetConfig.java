package com.poscoict.posledger.assets.config;

import com.poscoict.posledger.assets.client.ChannelClient;
import com.poscoict.posledger.assets.client.FabricClient;
import com.poscoict.posledger.assets.user.UserContext;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class SetConfig {
    static String owner;
    static String receiver;
    static Enrollment enrollment;

    static UserContext userContext;
    static FabricClient fabClient;

    public static UserContext initUserContext(String owner, Enrollment enrollment) {
        if(enrollment == null) {
            System.out.println("No enrollment");
            return null;
        }

        userContext = new UserContext();
        userContext.setName(owner);
        userContext.setAffiliation(Config.ORG1);
        userContext.setMspId(Config.ORG1_MSP);
        userContext.setEnrollment(enrollment);

        return userContext;
    }

    public static FabricClient getFabClient() throws IllegalAccessException, InvocationTargetException, InvalidArgumentException, InstantiationException, NoSuchMethodException, CryptoException, ClassNotFoundException {
        return new FabricClient(userContext);
    }

    /*
    public static ChannelClient initChannel() throws InvalidArgumentException, TransactionException {

        try {
            fabClient = new FabricClient(userContext);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
        Channel channel = channelClient.getChannel();

        Peer peer = fabClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
        Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
        EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", Config.EVENT_HUB);

        channel.addPeer(peer);
        channel.addEventHub(eventHub);
        channel.addOrderer(orderer);
        channel.initialize();

        return channelClient;
    }
     */


    public static ChannelClient initChannel(ArrayList<String> peerName, ArrayList<String> peerURL, ArrayList<String> ordererName, ArrayList<String> ordererURL, ArrayList<String> eventHubName, ArrayList<String> eventHubURL) throws InvalidArgumentException, TransactionException, TransactionException {

        try {
            fabClient = new FabricClient(userContext);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
        Channel channel = channelClient.getChannel();

        if(peerName != null) {
            for (int i = 0; i < peerName.size(); i++) {
                Peer peer = fabClient.getInstance().newPeer(peerName.get(i), peerURL.get(i));
                channel.addPeer(peer);
            }
        }

        if(ordererName != null) {
            for (int i = 0; i < ordererName.size(); i++) {
                Orderer orderer = fabClient.getInstance().newOrderer(ordererName.get(i), ordererURL.get(i));
                channel.addOrderer(orderer);
            }
        }

        if(eventHubName != null) {
            for (int i = 0; i < eventHubName.size(); i++) {
                EventHub eventHub = fabClient.getInstance().newEventHub(eventHubName.get(i), eventHubURL.get(i));
                channel.addEventHub(eventHub);
            }
        }

        channel.initialize();
        return channelClient;
    }

    public static void setEnrollment(String owner, Enrollment enrollment) {
        SetConfig.owner = owner;
        SetConfig.enrollment = enrollment;
    }

    public static void setEnrollmentForReceiver(String receiver, Enrollment enrollment) {
        SetConfig.receiver = receiver;
        SetConfig.enrollment = enrollment;
    }
}
