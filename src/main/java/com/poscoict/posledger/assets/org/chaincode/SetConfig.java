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
    static String newOwner;
    static String approved;
    static String operator;

    static Enrollment enrollment;
    static Enrollment enrollmentForNewOwner;
    static Enrollment enrollmentForApproved;
    static Enrollment enrollmentForOperator;

    static UserContext userContextForOwner;
    static UserContext userContextForNewOwner;
    static UserContext userContextForApproved;
    static UserContext userContextForOperator;

    static FabricClient fabClient;// = new FabricClient(userContext);

    public static void setEnrollment(String _owner, Enrollment _enrollment) {
        owner = _owner;
        enrollment = _enrollment;
    }

    public static void setEnrollmentForNewOwner(String _newOwner, Enrollment _enrollmentForNewOwner) {
        newOwner = _newOwner;
        enrollmentForNewOwner = _enrollmentForNewOwner;
    }

    public static void setEnrollmentForApproved(String _approved, Enrollment _enrollmentForApproved) {
        approved = _approved;
        enrollmentForApproved = _enrollmentForApproved;
    }

    public static void setEnrollmentForOperator(String _oprator, Enrollment _enrollmentForOperator) {
        operator = _oprator;
        enrollmentForOperator = _enrollmentForOperator;
    }

    public static UserContext initUserContextForOwner() throws Exception {

        //Enrollment enrollment = re.getEnrollment(owner);

        userContextForOwner = new UserContext();
        userContextForOwner.setName(owner);
        userContextForOwner.setAffiliation(Config.ORG1);
        userContextForOwner.setMspId(Config.ORG1_MSP);
        userContextForOwner.setEnrollment(enrollment);

        return userContextForOwner;
    }

    public static UserContext initUserContextForNewOwner() throws Exception {

        //Enrollment enrollment = re.getEnrollment(owner);

        userContextForNewOwner = new UserContext();
        userContextForNewOwner.setName(newOwner);
        userContextForNewOwner.setAffiliation(Config.ORG1);
        userContextForNewOwner.setMspId(Config.ORG1_MSP);
        userContextForNewOwner.setEnrollment(enrollmentForNewOwner);

        return userContextForNewOwner;
    }

    public static UserContext initUserContextForApproved() throws Exception {

        //Enrollment enrollment = re.getEnrollment(owner);

        userContextForApproved = new UserContext();
        userContextForApproved.setName(approved);
        userContextForApproved.setAffiliation(Config.ORG1);
        userContextForApproved.setMspId(Config.ORG1_MSP);
        userContextForApproved.setEnrollment(enrollmentForApproved);

        return userContextForApproved;
    }

    public static UserContext initUserContextForOperator() throws Exception {

        //Enrollment enrollment = re.getEnrollment(owner);

        userContextForOperator = new UserContext();
        userContextForOperator.setName(operator);
        userContextForOperator.setAffiliation(Config.ORG1);
        userContextForOperator.setMspId(Config.ORG1_MSP);
        userContextForOperator.setEnrollment(enrollmentForOperator);

        return userContextForOperator;
    }
    public static FabricClient getFabClient() {
        return fabClient;
    }

    public static ChannelClient initChannel() throws Exception {

        try {
            fabClient = new FabricClient(userContextForOwner);
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


}
