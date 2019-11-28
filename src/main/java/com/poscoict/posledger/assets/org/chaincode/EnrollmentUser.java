package com.poscoict.posledger.assets.org.chaincode;

import com.poscoict.posledger.assets.org.user.UserContext;
import com.poscoict.posledger.assets.service.RedisService;
import org.bouncycastle.util.io.pem.PemReader;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallet.Identity;
import org.hyperledger.fabric.protos.msp.Identities;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.identity.X509Identity;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.Set;

import static com.poscoict.posledger.assets.org.config.Config.CA_ORG1_URL;

public class EnrollmentUser {

    X509Identity a;
    String userID = null;
    @Autowired
    RedisService redisService;

    public void enrollAdmin() throws Exception {

        // Create a CA client for interacting with the CA.
        Properties props = new Properties();
        props.put("pemFile",
                "./fabric-samples/first-network/crypto-config/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem"/*"/root/fabric-samples/first-network/crypto-config/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem"*/);
        props.put("allowAllHostNames", "true");
        // if url starts as https.., need to set SSL
        HFCAClient caClient = HFCAClient.createNewInstance(CA_ORG1_URL, null/*props*/);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallet.createFileSystemWallet(Paths.get("wallet"));

        if(wallet == null)
            System.out.println("wallet fail");
        else
            System.out.println(wallet.toString()+"------------------------------");
        // Check to see if we've already enrolled the admin user.
        boolean adminExists = wallet.exists("admin");
        if (adminExists) {
            System.out.println("An identity for the admin user \"admin\" already exists in the wallet");
            return;
        }

        // Enroll the admin user, and import the new identity into the wallet.
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.addHost("localhost");
        enrollmentRequestTLS.setProfile("tls");
        org.hyperledger.fabric.sdk.Enrollment enrollment = caClient.enroll("admin", "adminpw", enrollmentRequestTLS);
        Identity user = Identity.createIdentity("Org1MSP", enrollment.getCert(), enrollment.getKey());
        wallet.put("admin", user);
        System.out.println("Successfully enrolled user \"admin\" and imported it into the wallet");
    }

    public Enrollment registerUser(String _userID) throws Exception {

        this.userID = _userID;

        // Create a CA client for interacting with the CA.
        Properties props = new Properties();
        props.put("pemFile",
                "./fabric-samples/first-network/crypto-config/peerOrganizations/org2.example.com/ca/ca.org2.example.com-cert.pem");
        props.put("allowAllHostNames", "true");
        // if url starts as https.., need to set SSL
        HFCAClient caClient = HFCAClient.createNewInstance(CA_ORG1_URL, null/*props*/);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
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
        Identity adminIdentity = wallet.get("admin");
        User admin = new User() {

            @Override
            public String getName() {
                return "admin";
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
                return "org1.department1";
            }

            @Override
            public org.hyperledger.fabric.sdk.Enrollment getEnrollment() {
                return new org.hyperledger.fabric.sdk.Enrollment() {

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
                return "hh";
            }
        }
                ;

        // Register the user, enroll the user, and import the new identity into the wallet.
        RegistrationRequest registrationRequest = new RegistrationRequest(this.userID);
        registrationRequest.setAffiliation("org1.department1");
        registrationRequest.setEnrollmentID(this.userID);
        String enrollmentSecret = caClient.register(registrationRequest, admin);
        org.hyperledger.fabric.sdk.Enrollment enrollment = caClient.enroll(this.userID, enrollmentSecret);
        Identity user = Identity.createIdentity("kk", enrollment.getCert(), enrollment.getKey());
        System.out.println("**********************"+enrollment.getCert()+"**************************");
        wallet.put(this.userID,user);
        System.out.println("Successfully enrolled user " + this.userID + " and imported it into the wallet");

        UserContext userContext = new UserContext();
        userContext.setName(this.userID);
        userContext.setAffiliation("org1.department1");
        userContext.setMspId("hh");
        userContext.setEnrollment(enrollment);
        a = new X509Identity(userContext);

        String addr = getMyAddress();
        System.out.println(addr);
        return enrollment;
    }

    private String getAddressOf(byte[] publicKey) {
        return AddressUtils.getAddressFor(publicKey);
    }

    public String getMyAddress() {
        return AddressUtils.getAddressFor(getMyCertificate());
    }

    public X509Certificate getMyCertificate() {
        try {
            Identities.SerializedIdentity identity = a.createSerializedIdentity();
            StringReader reader = new StringReader(identity.getIdBytes().toStringUtf8());
            PemReader pr = new PemReader(reader);
            byte[] x509Data = pr.readPemObject().getContent();
            CertificateFactory factory = CertificateFactory.getInstance("X509");
            return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(x509Data));
        } catch (IOException | CertificateException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) throws Exception {

    }
}
