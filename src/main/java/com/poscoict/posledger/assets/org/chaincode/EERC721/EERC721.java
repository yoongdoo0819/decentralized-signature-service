package com.poscoict.posledger.assets.org.chaincode.EERC721;

import com.poscoict.posledger.assets.org.chaincode.InvokeChaincode;
import com.poscoict.posledger.assets.org.chaincode.QueryChaincode;
import com.poscoict.posledger.assets.org.chaincode.SetConfig;
import com.poscoict.posledger.assets.org.client.ChannelClient;
import com.poscoict.posledger.assets.org.client.FabricClient;
import com.poscoict.posledger.assets.org.config.Config;
import com.poscoict.posledger.assets.service.RedisService;
import org.hyperledger.fabric.sdk.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class EERC721 {

    @Autowired
    RedisService redisService;

    private static final byte[] EXPECTED_EVENT_DATA = "!".getBytes(UTF_8);
    private static final String EXPECTED_EVENT_NAME = "event";

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

    public String register(String tokenId, String type, String owner, String page, String hash, String signers, String path, String pathHash) {

        String result = "";
        try {

            ChannelClient channelClient = SetConfig.initChannel();
            FabricClient fabClient = SetConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("mint");
            String[] arguments = { tokenId, type, owner, page, hash, signers, path, pathHash};

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
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"mint on "+Config.CHAINCODE_1_NAME + ". Status - " + status + " result - " + res.getMessage());
                result = res.getMessage();
                //result = (String)res.getStatus();
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = "Failure";
        }

        return result;
    }

    public String balanceOf(String owner, String type) {

        String result = "";
        try {
            ChannelClient channelClient = SetConfig.initChannel();
            FabricClient fabClient = SetConfig.getFabClient();

            Thread.sleep(1000);
            Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode("mycc", "balanceOf", new String[]{owner, type});
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

    public String deactivate(String tokenId) {

        String result = "";
        try {
            ChannelClient channelClient = SetConfig.initChannel();
            FabricClient fabClient = SetConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
            request.setChaincodeID(ccid);
            //request.setFcn("deactivate");
            //String[] arguments = { tokenId };

            request.setFcn("deactivate");
            String[] arguments = { tokenId };
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
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"deactivated on "+Config.CHAINCODE_1_NAME + ". Status - " + status);

                result = res.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.getLogger(result);
        return result;
    }

    public String divide(String tokenId, String[] newIds, String[] values, int index) {

        String result = "";
        try {
            ChannelClient channelClient = SetConfig.initChannel();
            FabricClient fabClient = SetConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("divide");
            String[] arguments = { tokenId,  Arrays.toString(newIds), Arrays.toString(values), Integer.toString(index) };
            //request.setFcn("createCar");
            //String[] arguments = { "CAR1", "Chevy", "Volt", "Red", "Nick" };
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
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"divided on "+Config.CHAINCODE_1_NAME + ". Status - " + status);
                result = res.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.getLogger(result);
        return result;
    }

    public String update(String tokenId, String index, String attr) {

        String result = "";
        try {
            ChannelClient channelClient = SetConfig.initChannel();
            FabricClient fabClient = SetConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn("setXAttr");
            String[] arguments = { tokenId, index, attr };
            //request.setFcn("createCar");
            //String[] arguments = { "CAR1", "Chevy", "Volt", "Red", "Nick" };
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
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO,"divided on "+Config.CHAINCODE_1_NAME + ". Status - " + status);
                result = res.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.getLogger(result);
        return result;
    }

    public String query(String tokenId) {

        String result = "";
        try {
            ChannelClient channelClient = SetConfig.initChannel();
            FabricClient fabClient = SetConfig.getFabClient();

            Thread.sleep(1000);
            Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode("mycc", "query", new String[]{tokenId});
            for (ProposalResponse pres : responses1Query) {
                //String stringResponse2 = new String(pres.getChaincodeActionResponsePayload());
                Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, pres.getMessage());

                result = pres.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.getLogger(result);
        return result;
    }

    public String queryHistory(String tokenId, String owner) {

        String result = "";
        try {
            ChannelClient channelClient = SetConfig.initChannel();
            FabricClient fabClient = SetConfig.getFabClient();

            Thread.sleep(1000);
            Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Query token ");

            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode("mycc", "queryHistory", new String[]{tokenId});
            for (ProposalResponse pres : responses1Query) {
                //String stringResponse2 = new String(pres.getChaincodeActionResponsePayload());
                Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, pres.getMessage());

                result = pres.getMessage();
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
