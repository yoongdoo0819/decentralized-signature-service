package com.github.fabasset.chaincode;

import com.github.fabasset.client.CAClient;
import com.github.fabasset.service.RedisService;
import com.github.fabasset.util.AddressUtils;
import com.github.fabasset.config.NetworkConfig;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallet.Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.identity.X509Identity;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Set;

import static com.github.fabasset.config.NetworkConfig.ORG_MSP;

public class EnrollUser {

    X509Identity identity;
    String userId = null;

    AddressUtils addressUtils = new AddressUtils();
    @Autowired
    RedisService redisService;

    public Enrollment enrollAdmin(String userId, String passwd) throws Exception {

        // Create a CA client for interacting with the CA.
//        Properties props = new Properties();
//        props.put("pemFile",
//                "/home/yoongdoo0819/fabric-network/network/crypto-config/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem"/*"/root/fabric-samples/first-network/crypto-config/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem"*/);
//        props.put("allowAllHostNames", "true");
        // if url starts as https.., need to set SSL

//        HFCAClient caClient = HFCAClient.createNewInstance(Config.CA_ORG1_URL, null/*props*/);
//        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
//        caClient.setCryptoSuite(cryptoSuite);


        CAClient caClient = new CAClient(NetworkConfig.CA_ORG_URL, null);
        // Create a wallet for managing identities
        Wallet wallet = Wallet.createFileSystemWallet(Paths.get("wallet"));

        /*
        if(wallet == null)
            System.out.println("wallet fail");
        else
            System.out.println(wallet.toString()+"------------------------------");
        // Check to see if we've already enrolled the admin user.
        boolean adminExists = wallet.exists(userId);
        if (adminExists) {
            System.out.println("An identity for the admin user \"admin\" already exists in the wallet");
            return null;
        }

         */

        // Enroll the admin user, and import the new identity into the wallet.
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.addHost("localhost");
        enrollmentRequestTLS.setProfile("tls");
        Enrollment enrollment = caClient.getInstance().enroll(userId, passwd, enrollmentRequestTLS);
        Identity user = Identity.createIdentity(ORG_MSP, enrollment.getCert(), enrollment.getKey());
        wallet.put(userId, user);
        //System.out.println("Successfully enrolled user \"admin\" and imported it into the wallet");

        return enrollment;
    }

    public Enrollment registerUser(String userId) throws Exception {

        this.userId = userId;
        CAClient caClient = new CAClient(NetworkConfig.CA_ORG_URL, null);

        // Create a CA client for interacting with the CA.
//        Properties props = new Properties();
//        props.put("pemFile",
//                "./fabric-samples/first-network/crypto-config/peerOrganizations/org2.example.com/ca/ca.org2.example.com-cert.pem");
//        props.put("allowAllHostNames", "true");
//        // if url starts as https.., need to set SSL
//        HFCAClient caClient = HFCAClient.createNewInstance(Config.CA_ORG1_URL, null/*props*/);
//        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
//        caClient.setCryptoSuite(cryptoSuite);
//
//        // Create a wallet for managing identities
        Wallet wallet = Wallet.createFileSystemWallet(Paths.get("wallet"));
/*
        // Check to see if we've already enrolled the user.
        boolean userExists = wallet.exists(this.userID);
        if (userExists) {
            System.out.println("An identity for the user " + this.userID + " already exists in the wallet");
            return null;
        }

        userExists = wallet.exists("admin");
        if (!userExists) {
            System.out.println("\"admin\" needs to be enrolled and added to the wallet first");
            return null;
        }
*/

        Identity adminIdentity = wallet.get(NetworkConfig.ADMIN);
        User admin = new User() {

            @Override
            public String getName() {
                return NetworkConfig.ADMIN;
            }

            @Override
            public Set<String> getRoles() {
                return null;
            }

            @Override
            public String getAccount() {
                return null;
            }

            @Override
            public String getAffiliation() {
                return null;//"org0.department1";
            }

            @Override
            public Enrollment getEnrollment() {
                return new Enrollment() {

                    @Override
                    public PrivateKey getKey() {
                        return adminIdentity.getPrivateKey();
                    }

                    @Override
                    public String getCert() {
                        return adminIdentity.getCertificate();
                    }
                };
            }

            @Override
            public String getMspId() {
                return ORG_MSP;
            }
        }
                ;

//        UserContext admin2 = new UserContext(); //Util.readUserContext("org1.department1", Config.ADMIN);
//        admin2.setEnrollment(adminIdentity.getCertificate().);
//
        // Register the user, enroll the user, and import the new identity into the wallet.
        RegistrationRequest registrationRequest = new RegistrationRequest(this.userId);
        //registrationRequest.setAffiliation("org0.department1");
        registrationRequest.setEnrollmentID(this.userId);
        String enrollmentSecret = caClient.getInstance().register(registrationRequest, admin);
        Enrollment enrollment = caClient.getInstance().enroll(this.userId, enrollmentSecret);
        Identity user = Identity.createIdentity(ORG_MSP, enrollment.getCert(), enrollment.getKey());
        System.out.println("**********************"+enrollment.getCert()+"**************************");
        //wallet.put(this.userID, user);
        //System.out.println("Successfully enrolled user " + this.userID + " and imported it into the wallet");

//        UserContext userContext = new UserContext();
//        userContext.setName(this.userID);
//        userContext.setAffiliation("org1.department1");
//        userContext.setMspId("Org1MSP");
//        userContext.setEnrollment(enrollment);
//        identity = new X509Identity(userContext);

        String addr = addressUtils.getMyAddress(this.userId, enrollment);
        System.out.println(addr);
        return enrollment;
    }

    public static void main(String[] args) throws Exception {

    }
}
