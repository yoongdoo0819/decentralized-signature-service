package com.poscoict.posledger.assets.config;

import com.poscoict.posledger.assets.chaincode.ChaincodeProxy;
import com.poscoict.posledger.assets.util.RedisEnrollment;
import com.poscoict.posledger.assets.client.ChannelClient;
import com.poscoict.posledger.assets.client.FabricClient;
import com.poscoict.posledger.assets.user.UserContext;
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
        userContext.setAffiliation(NetworkConfig.ORG0);
        userContext.setMspId(NetworkConfig.ORG0_MSP);
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


    public static ChannelClient initChannel() throws InvalidArgumentException, TransactionException, TransactionException  {//ArrayList<String> peerName, ArrayList<String> peerURL, ArrayList<String> ordererName, ArrayList<String> ordererURL, ArrayList<String> eventHubName, ArrayList<String> eventHubURL) throws InvalidArgumentException, TransactionException, TransactionException {


        try {
            fabClient = new FabricClient(userContext);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        /*
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

         */
        /*
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

         */

        ChannelClient channelClient = fabClient.createChannelClient(NetworkConfig.CHANNEL_NAME);
        Channel channel = channelClient.getChannel();

        Peer peer = fabClient.getInstance().newPeer(NetworkConfig.ORG0_PEER_0, NetworkConfig.ORG0_PEER_0_URL);
        Orderer orderer = fabClient.getInstance().newOrderer(NetworkConfig.ORDERER_NAME, NetworkConfig.ORDERER_URL);
        EventHub eventHub = fabClient.getInstance().newEventHub("Transfer", NetworkConfig.EVENT_HUB);

        channel.addPeer(peer);
        channel.addEventHub(eventHub);
        channel.addOrderer(orderer);
        channel.initialize();
        //channel.registerBlockListener(blockListener);
        //channel.registerChaincodeEventListener(Pattern.compile(".*"), Pattern.compile(Pattern.quote("Transfer")), chaincodeEventListener);
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
        ExecutionConfig.owner = owner;
        ExecutionConfig.enrollment = enrollment;
    }

    public static void setEnrollmentForReceiver(String receiver, Enrollment enrollment) {
        ExecutionConfig.receiver = receiver;
        ExecutionConfig.enrollment = enrollment;
    }

    public static ChaincodeProxy initChaincodeProxy(String userId, Enrollment enrollment) throws IllegalAccessException, InvalidArgumentException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, CryptoException, TransactionException {

        ExecutionConfig.initUserContext(userId, enrollment);
        //Manager.setChaincodeId(chaincodeId);
        //FabricClient fabricClient = getFabClient();
        ChannelClient channelClient = ExecutionConfig.initChannel();
        ChaincodeProxy chaincodeProxy = new ChaincodeProxy();
        chaincodeProxy.setFabricClient(fabClient);
        chaincodeProxy.setChannelClient(channelClient);

        return chaincodeProxy;
    }
}
