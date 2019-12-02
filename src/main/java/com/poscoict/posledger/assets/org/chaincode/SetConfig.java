package com.poscoict.posledger.assets.org.chaincode;

import com.poscoict.posledger.assets.org.client.ChannelClient;
import com.poscoict.posledger.assets.org.client.FabricClient;
import com.poscoict.posledger.assets.org.config.Config;
import com.poscoict.posledger.assets.org.user.UserContext;
import org.hyperledger.fabric.sdk.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetConfig {

    @Autowired
    RedisEnrollment re;

    static String owner;
    static String receiver;
    static Enrollment enrollment;

    static UserContext userContext;
    static FabricClient fabClient;// = new FabricClient(userContext);

    public static UserContext initUserContext() throws Exception {

        //Enrollment enrollment = re.getEnrollment(owner);
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

    public static FabricClient getFabClient() {
        return fabClient;
    }

    public static ChannelClient initChannel() throws Exception {

        try {
            fabClient = new FabricClient(userContext);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
        Channel channel = channelClient.getChannel();
        Peer peer = fabClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
        EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", Config.EVENT_HUB);
        Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
        channel.addPeer(peer);
        channel.addEventHub(eventHub);
        channel.addOrderer(orderer);
        channel.initialize();

        return channelClient;
    }

    public static void setEnrollment(String _owner, Enrollment _enrollment) {
        owner = _owner;
        enrollment = _enrollment;
    }

    public static void setEnrollmentForReceiver(String _receiver, Enrollment _enrollment) {
        receiver = _receiver;
        enrollment = _enrollment;
    }
}
