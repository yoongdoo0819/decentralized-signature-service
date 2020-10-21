package com.poscoict.posledger.assets.config;

import com.poscoict.posledger.assets.client.ChannelClient;
import com.poscoict.posledger.assets.client.FabricClient;
import com.poscoict.posledger.assets.user.UserContext;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.scheduling.annotation.Async;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.regex.Pattern;

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

        ChaincodeEventListener chaincodeEventListener = new ChaincodeEventListener() {

            @Async
            @Override
            public void received(String s, BlockEvent blockEvent, ChaincodeEvent chaincodeEvent) {

                //System.out.println(" >> " + blockEvent.getBlock().getAllFields());
                //System.out.println(" >> " + s);
                //System.out.println(" >> " + Arrays.toString(chaincodeEvent.getPayload()));
                //System.out.println(" >> " + new String(chaincodeEvent.getPayload()));

                System.out.format(
                        ", thread name: %s"
                                + ", chaincode Id: %s"
                                + ", chaincode event name: %s"
                                + ", transaction id: %s"
                                + ", event payload: \"%s\"",
                        Thread.currentThread().getName(),

                        chaincodeEvent.getChaincodeId(),
                        chaincodeEvent.getEventName(),
                        chaincodeEvent.getTxId(),
                        new String(chaincodeEvent.getPayload())
                        );
            }
        };

        BlockListener blockListener = new BlockListener() {
            @Override
            public void received(BlockEvent blockEvent) {
                Iterable<BlockEvent.TransactionEvent> txs = blockEvent.getTransactionEvents();
                Common.Block block = blockEvent.getBlock();

                System.out.println("Event Success#################");
                System.out.println(block.getData().getDataList().toString());

                for (BlockEvent.TransactionEvent txEvent: txs) {
                    System.out.println(txEvent.getCreator());
                    System.out.println(txEvent.toString());
                    System.out.println(txEvent.getCreator());
                    System.out.println(txEvent.getTransactionActionInfo(0));
                    System.out.println(txEvent.getBlockEvent());
                    System.out.println(txEvent.getSignature());
                }
//                System.out.println("Event Success#################");
//                System.out.println(block.getData());
//                System.out.println(block.getData());
            }
        };

        ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
        Channel channel = channelClient.getChannel();

        Peer peer = fabClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
        Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
        EventHub eventHub = fabClient.getInstance().newEventHub("Transfer", Config.EVENT_HUB);

        channel.addPeer(peer);
        channel.addEventHub(eventHub);
        channel.addOrderer(orderer);
        channel.initialize();
        //channel.registerBlockListener(blockListener);
        channel.registerChaincodeEventListener(Pattern.compile(".*"), Pattern.compile(Pattern.quote("Transfer")), chaincodeEventListener);
        return channelClient;
        /*
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

         */
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
