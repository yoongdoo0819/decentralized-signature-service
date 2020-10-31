package com.github.fabasset.example.config;

import com.github.fabasset.example.util.RedisEnrollment;
import com.github.fabasset.sdk.user.UserContext;
import com.github.fabasset.sdk.client.FabricClient;
import com.github.fabasset.sdk.chaincode.ChaincodeProxy;
import com.github.fabasset.sdk.client.ChannelClient;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;

public class ExecutionConfig {
    static String owner;
    static String receiver;
    static Enrollment enrollment;

    static UserContext userContext;
    static FabricClient fabClient;

    @Autowired
    private RedisEnrollment re;

    public static UserContext initUserContext(String owner, Enrollment enrollment) {
        if(enrollment == null) {
            System.out.println("No enrollment");
            return null;
        }

        userContext = new UserContext();
        userContext.setName(owner);
        userContext.setAffiliation(NetworkConfig.ORG);
        userContext.setMspId(NetworkConfig.ORG_MSP);
        userContext.setEnrollment(enrollment);

        return userContext;
    }

    public static FabricClient getFabClient() throws IllegalAccessException, InvocationTargetException, InvalidArgumentException, InstantiationException, NoSuchMethodException, CryptoException, ClassNotFoundException {
        return new FabricClient(userContext);
    }



    public static ChannelClient initChannel() throws InvalidArgumentException, TransactionException, TransactionException  {//ArrayList<String> peerName, ArrayList<String> peerURL, ArrayList<String> ordererName, ArrayList<String> ordererURL, ArrayList<String> eventHubName, ArrayList<String> eventHubURL) throws InvalidArgumentException, TransactionException, TransactionException {


        try {
            fabClient = new FabricClient(userContext);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        ChannelClient channelClient = fabClient.createChannelClient(NetworkConfig.CHANNEL_NAME);
        Channel channel = channelClient.getChannel();

        Peer peer = fabClient.getInstance().newPeer(NetworkConfig.ORG_PEER, NetworkConfig.ORG_PEER_URL);
        Orderer orderer = fabClient.getInstance().newOrderer(NetworkConfig.ORDERER_NAME, NetworkConfig.ORDERER_URL);
        EventHub eventHub = fabClient.getInstance().newEventHub("Transfer", NetworkConfig.EVENT_HUB);

        System.out.println(NetworkConfig.ORG_MSP);
        System.out.println(NetworkConfig.ORG);
        System.out.println(NetworkConfig.ORG_PEER);
        System.out.println(NetworkConfig.ORG_PEER_URL);
        System.out.println(NetworkConfig.CA_ORG_URL);
        System.out.println(NetworkConfig.ORDERER_NAME);
        System.out.println(NetworkConfig.ORDERER_URL);
        System.out.println(NetworkConfig.ADMIN);
        System.out.println(NetworkConfig.CHANNEL_NAME);
        System.out.println(NetworkConfig.CHAINCODE_NAME);

        channel.addPeer(peer);
        channel.addEventHub(eventHub);
        channel.addOrderer(orderer);
        channel.initialize();
        return channelClient;

    }

    public static void setEnrollment(String owner, Enrollment enrollment) {
        ExecutionConfig.owner = owner;
        ExecutionConfig.enrollment = enrollment;
    }

    public static void setEnrollmentForReceiver(String receiver, Enrollment enrollment) {
        ExecutionConfig.receiver = receiver;
        ExecutionConfig.enrollment = enrollment;
    }

    public static ChaincodeProxy initChaincodeProxy(String userId, Enrollment enrollment) throws IllegalAccessException, InvalidArgumentException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, CryptoException, TransactionException {

        ExecutionConfig.initUserContext(userId, enrollment);
        ChannelClient channelClient = ExecutionConfig.initChannel();
        ChaincodeProxy chaincodeProxy = new ChaincodeProxy();
        chaincodeProxy.setFabricClient(fabClient);
        chaincodeProxy.setChannelClient(channelClient);

        return chaincodeProxy;
    }
}
