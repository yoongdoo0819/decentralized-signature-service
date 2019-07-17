package com.poscoict.posledger.assets.org.app.chaincode.invocation;

import com.poscoict.posledger.assets.org.app.client.CAClient;
import com.poscoict.posledger.assets.org.app.client.ChannelClient;
import com.poscoict.posledger.assets.org.app.client.FabricClient;
import com.poscoict.posledger.assets.org.app.config.Config;
import com.poscoict.posledger.assets.org.app.user.UserContext;
import com.poscoict.posledger.assets.org.app.util.Util;
import org.hyperledger.fabric.sdk.*;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

public class queryTokensByOwner {

    private static final byte[] EXPECTED_EVENT_DATA = "!".getBytes(UTF_8);
    private static final String EXPECTED_EVENT_NAME = "event";

    public static void main(String args[]) {
        try {
            Util.cleanUp();
            String caUrl = Config.CA_ORG1_URL;
            CAClient caClient = new CAClient(caUrl, null);
            // Enroll Admin to Org1MSP
            UserContext adminUserContext = new UserContext();
            adminUserContext.setName(Config.ADMIN);
            adminUserContext.setAffiliation(Config.ORG1);
            adminUserContext.setMspId(Config.ORG1_MSP);
            caClient.setAdminUserContext(adminUserContext);
            adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

            FabricClient fabClient = new FabricClient(adminUserContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();
            Peer peer = fabClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
            EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
            Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
            channel.addPeer(peer);
            channel.addEventHub(eventHub);
            channel.addOrderer(orderer);
            channel.initialize();

            //Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Query a");
			/*Collection<ProposalResponse>  responsesQuery = channelClient.queryByChainCode("fabcar", "query", new String[]{"a"});
			for (ProposalResponse pres : responsesQuery) {
				String stringResponse = new String(pres.getChaincodeActionResponsePayload());
				Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, stringResponse);
			}*/

            Thread.sleep(10000);
            Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode("mycc3", "queryTokensByOwner", new String[]{"sangwon"});
            for (ProposalResponse pres : responses1Query) {
                String stringResponse = new String(pres.getChaincodeActionResponsePayload());
                Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, stringResponse);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
