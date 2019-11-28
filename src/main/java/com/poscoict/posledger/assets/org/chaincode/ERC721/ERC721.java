package com.poscoict.posledger.assets.org.chaincode.ERC721;

import com.poscoict.posledger.assets.org.chaincode.InvokeChaincode;
import com.poscoict.posledger.assets.org.chaincode.QueryChaincode;
import com.poscoict.posledger.assets.org.chaincode.SetConfig;
import com.poscoict.posledger.assets.org.client.ChannelClient;
import com.poscoict.posledger.assets.org.client.FabricClient;
import com.poscoict.posledger.assets.org.config.Config;
import com.poscoict.posledger.assets.service.RedisService;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.ChaincodeResponse;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;


@Component
public class ERC721 {

    private static final byte[] EXPECTED_EVENT_DATA = "!".getBytes(UTF_8);
    private static final String EXPECTED_EVENT_NAME = "event";

    @Autowired
    RedisService redisService;

    /*
    public Enrollment getEnrollment(String owner) throws Exception {
        String certificate = redisService.getUserInfo(owner);
        Enrollment enrollment;
        byte[] serializedMember = Base64.getDecoder().decode(certificate);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedMember)) {
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                //deserialize
                Object objectMember = ois.readObject();
                enrollment = (Enrollment) objectMember;
                System.out.println(enrollment);

                return enrollment;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
     */

    public String register(String tokenId, String owner) {

        String result = "";
        try {

            ChannelClient channelClient = SetConfig.initChannel();
            FabricClient fabClient = SetConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("mint");
            String[] arguments = { tokenId, owner};

            request.setArgs(arguments);
            /*request.setProposalWaitTime(1000);


            Map<String, byte[]> tm2 = new HashMap<>();
            tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
            tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
            tm2.put("result", ":)".getBytes(UTF_8));
            tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA);
            request.setTransientMap(tm2);

             */
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"mint on "+Config.CHAINCODE_1_NAME + ". Status - " + status + " Message - " + res.getMessage());
                result = res.getMessage();
                //result = (String)res.getStatus();
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = "Failure";
        }

        return result;
    }

    public String balanceOf(String owner) {

        String result = "";
        try {
            ChannelClient channelClient = SetConfig.initChannel();
            FabricClient fabClient = SetConfig.getFabClient();

            Thread.sleep(10000);
            Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode("mycc", "balanceOf", new String[]{owner});
            for (ProposalResponse pres : responses1Query) {
                //String stringResponse = new String(pres.getChaincodeActionResponsePayload());
                Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String ownerOf(String tokenId) {

        String result = "";
        try {
            ChannelClient channelClient = SetConfig.initChannel();
            FabricClient fabClient = SetConfig.getFabClient();

            Thread.sleep(10000);
            Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.CHAINCODE_1_NAME, "ownerOf", new String[]{tokenId});
            for (ProposalResponse pres : responses1Query) {
                //String stringResponse = new String(pres.getChaincodeActionResponsePayload());
                Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.getLogger(result);
        return result;
    }

    public String approve(String approved, String tokenId) {
        String result = "";
        try {

            ChannelClient channelClient = SetConfig.initChannel();
            FabricClient fabClient = SetConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("approve");
            String[] arguments = { approved, tokenId };

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Map<String, byte[]> tm2 = new HashMap<>();
            tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
            tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
            tm2.put("result", ":)".getBytes(UTF_8));
            tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA);
            request.setTransientMap(tm2);
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"approve on "+Config.CHAINCODE_1_NAME + ". Status - " + status);
                result = res.getMessage();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.getLogger(result);
        return result;
    }

    public String getApproved(String tokenId) {

        String result="";
        try {
            ChannelClient channelClient = SetConfig.initChannel();
            FabricClient fabClient = SetConfig.getFabClient();

            Thread.sleep(1000);
            Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.CHAINCODE_1_NAME, "getApproved", new String[]{tokenId});
            for (ProposalResponse pres : responses1Query) {
                //String stringResponse = new String(pres.getChaincodeActionResponsePayload());
                Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, pres.getMessage());
                //result = stringResponse;
                result = pres.getMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.getLogger(result);
        return result;
    }

    public String setApprovalForAll(String owner, String operator, String approved) {
        String result = "";
        try {
            ChannelClient channelClient = SetConfig.initChannel();
            FabricClient fabClient = SetConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("setApprovalForAll");
            String[] arguments = { owner, operator , approved};

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Map<String, byte[]> tm2 = new HashMap<>();
            tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
            tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
            tm2.put("result", ":)".getBytes(UTF_8));
            tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA);
            request.setTransientMap(tm2);
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"setApprovalForAll on "+Config.CHAINCODE_1_NAME + ". Status - " + status);
                result = res.getMessage();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.getLogger(result);
        return result;
    }

    public String isApprovedForAll(String owner, String operator) {
        String result = "";
        try {
            ChannelClient channelClient = SetConfig.initChannel();
            FabricClient fabClient = SetConfig.getFabClient();

            Thread.sleep(10000);
            Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode("mycc", "isApprovedForAll", new String[]{owner, operator});
            for (ProposalResponse pres : responses1Query) {
                //String stringResponse = new String(pres.getChaincodeActionResponsePayload());
                Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.getLogger(result);
        return result;
    }

    public String transfer(String owner, String receiver, String tokenId) {
        String result = "";
        try {
            ChannelClient channelClient = SetConfig.initChannel();
            FabricClient fabClient = SetConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("transferFrom");
            String[] arguments = { owner, receiver , tokenId};

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Map<String, byte[]> tm2 = new HashMap<>();
            tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
            tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
            tm2.put("result", ":)".getBytes(UTF_8));
            tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA);
            request.setTransientMap(tm2);
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"transferFrom on "+Config.CHAINCODE_1_NAME + ". Status - " + status);
                result = res.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.getLogger(result);
        return result;
    }

    public static void main(String args[]) {

    }
}
